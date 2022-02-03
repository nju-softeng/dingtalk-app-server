package com.softeng.dingtalk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeng.dingtalk.controller.WebSocketController;
import com.softeng.dingtalk.api.MessageApi;
import com.softeng.dingtalk.entity.*;

import com.softeng.dingtalk.enums.Position;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.vo.VoteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhanyeye
 * @description: 内部评审论文投票的相关业务逻辑
 * @create 2/6/2020 5:30 PM
 */
@Service
@Transactional
@Slf4j
public class VoteService {
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    VoteDetailRepository voteDetailRepository;
    @Autowired
    InternalPaperRepository internalPaperRepository;
    @Autowired
    PaperDetailRepository paperDetailRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    NotifyService notifyService;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    PaperService paperService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ExternalPaperRepository externalPaperRepository;

    @Autowired
    MessageApi messageApi;

    private final ReentrantLock lock = new ReentrantLock();

    private static final String VOTE_INFO_MARKDOWN = " #### 投票 \n ##### 论文：  %s \n ##### 作者： %s \n 截止时间: %s";
    private static final String VOTE_INFO_URL = "/paper/in-detail/%d/vote";

    /**
     * 创建论文评审投票
     * @param vo
     * @return
     */
    @CacheEvict(value = "voting", allEntries = true)
    public Vote createVote(VoteVO vo) {
        lock.lock();
        try {
            // 确保线程安全问题
            if (voteRepository.isExisted(vo.getPaperid(), false) == 0) {
                // 如果投票还没有被创建
                Vote vote = new Vote(LocalDateTime.now(), vo.getEndTime(), vo.getPaperid());
                voteRepository.save(vote);
                internalPaperRepository.updatePaperVote(vo.getPaperid(), vote.getId());
                sendVoteInfoCardToDingtalk(vo.getPaperid(), vo.getEndTime().toLocalTime());
                return voteRepository.refresh(vote);
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "慢了一步，投票已经被别人发起了");
            }
        } finally {
            lock.unlock();
        }
    }


    /**
     * 向钉钉群中发送投票消息卡片
     * @param paperId
     */
    private void sendVoteInfoCardToDingtalk(int paperId, LocalTime endTime) {
        messageApi.sendActionCard(
                "内部评审投票",
                String.format(
                        VOTE_INFO_MARKDOWN,
                        internalPaperRepository.getPaperTitleById(paperId),
                        paperDetailRepository.listPaperAuthor(paperId).toString(),
                        endTime.toString()),
                "前往投票",
                String.format(VOTE_INFO_URL, paperId)
        );
    }


    /**
     *
     * @param now
     * @return
     */
    public List<Vote> listUpcomingVote(LocalDateTime now) {
        return voteRepository.listUpcomingVote(now);
    }


    /**
     * 查询没有结束的投票,但即将结束的投票
     * 缓存未结束的投票，用于减少查询数据的次数，当创建新投票后要清空缓存
     * @param now
     * @return
     */
    @Cacheable(value = "voting")
    public List<Vote> listUnderwayVote(LocalDateTime now) {
        return voteRepository.listClosingVote(now);
    }


    /**
     * 更新投票的最终结果，投票截止后调用, 被定时器调用
     * @param vote
     * @return
     */
    @CacheEvict(value = "voting", allEntries = true)
    public Vote updateVote(Vote vote) {
        log.debug("投票结果更新，清空缓存");
        vote.setStatus(true);
        vote.setAccept(voteDetailRepository.getAcceptCnt(vote.getId()));
        vote.setTotal(voteDetailRepository.getCnt(vote.getId()));
        vote.setResult(getVotingResult(vote));
        voteRepository.save(vote);

        // 如果是内部评审投票
        if (!vote.isExternal()) {
            internalPaperRepository.updatePaperResult(
                    vote.getPid(),
                    vote.getResult() ? InternalPaper.REVIEWING : InternalPaper.NOTPASS
            );
        }

        try {
            WebSocketController.sendInfo(objectMapper.writeValueAsString(Map.of(
                    "vid", vote.getId(),
                    "isEnd", true
            )));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vote;
    }


    /**
     * 用户投票
     * @param vid
     * @param uid
     * @param voteDetail
     * @return
     */
    public Map poll(int vid, int uid, VoteDetail voteDetail) {
        // 收到投票的时间
        LocalDateTime now = LocalDateTime.now();
        Vote vote = voteRepository.findById(vid).get();

        // 如果是内部评审论文，判断投票人是否为论文作者
        if(paperService.listAuthorId(vote.getPid()).contains(uid)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "论文作者不能参与投票！");
        }

        // 投票已经截止
        if(now.isAfter(vote.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "投票已经截止！");
        }

        voteDetailRepository.save(voteDetail);
        return getVotingDetails(vid, uid);
    }

    /**
     * 不同的职位对应不同的票权
     * @param user 用户
     * @return 票权值
     */
    public static double getWeight(User user) {
        switch (user.getPosition()) {
            case DOCTOR:        return 2.0;
            case ACADEMIC:
            case PROFESSIONAL:  return 1.0;
            default:            return 0.0;
        }
    }

    /**
     * 根据投接受票的投拒绝票的人加权后算出接受的百分比值
     * @param acceptUserList 投接受的人的列表
     * @param rejectUserList 投拒绝的人的列表
     * @return [0, 1)
     */
    public double calculatePercentageOfVotesAccepted(List<User> acceptUserList, List<User> rejectUserList) {
        double acceptWeights = acceptUserList.stream().mapToDouble(VoteService::getWeight).sum();
        double rejectWeights = rejectUserList.stream().mapToDouble(VoteService::getWeight).sum();
        double totleWeights  = acceptWeights + rejectWeights;
        return totleWeights == 0.0 ? 0.0 : acceptWeights / totleWeights;
    }

    /**
     * 算出某次投票的接受的加权百分比
     * @param vote
     * @return [0, 1)
     */
    public double calculatePercentageOfVotesAccepted(Vote vote) {
        return calculatePercentageOfVotesAccepted(
                voteDetailRepository.listAcceptUserlist(vote.getId()),
                voteDetailRepository.listRejectUserlist(vote.getId())
        );
    }

    /**
     * 判断某次投票是否通过
     * @param vote
     * @return
     */
    public boolean getVotingResult(Vote vote) {
        return calculatePercentageOfVotesAccepted(vote) > 2.0 / 3.0;
    }


    /**
     * 获取投票详情
     * @param vid 某次投票的id
     * @param uid 用户id
     * @return map 待重构为具体对象
     */
    public Map getVotingDetails(int vid, int uid) {
        Vote vote = voteRepository.findById(vid).get();
        // myVote 我的投票，in (accept, reject, unvote)
        Boolean myVote = voteDetailRepository.getVoteDetail(vid, uid);
        // isOver 投票是否结束
        Boolean isOver = vote.getEndTime().isBefore(LocalDateTime.now());

        List<User> acceptUserList = new ArrayList<>();
        List<User> rejectUserList = new ArrayList<>();
        List<String> unVoteNames  = new ArrayList<>();

        double acceptedPercentage = 0.0;

        if(isOver) {
            // 投票已结束
            acceptUserList      = voteDetailRepository.listAcceptUserlist(vid);
            rejectUserList      = voteDetailRepository.listRejectUserlist(vid);
            unVoteNames         = voteDetailRepository.findUnVoteUsername(vid);
            acceptedPercentage  = calculatePercentageOfVotesAccepted(acceptUserList, rejectUserList);
        }
        return Map.of(
                "vid",                vid,
                "status",             isOver,
                "accept",             acceptUserList.size(),
                "reject",             rejectUserList.size(),
                "total",              acceptUserList.size() + rejectUserList.size(),
                "myvote",             myVote == null ? "unvote" : (myVote ? "accept" : "reject"),
                "acceptnames",        acceptUserList.stream().map(User::getName).collect(Collectors.toList()),
                "rejectnames",        rejectUserList.stream().map(User::getName).collect(Collectors.toList()),
                "unvotenames",        unVoteNames,
                "acceptedPercentage", acceptedPercentage
        );
    }

    /**
     * 生成投票的ac记录
     * @param title
     * @param user
     * @param isRight
     * @param dateTime
     * @return
     */
    private AcRecord generateAcRecord(String title, User user, boolean isRight, LocalDateTime dateTime) {
        return AcRecord.builder()
                .user(user)
                // 论文投票AC变化，对于硕士生是1分，对于博士生是2分
                .ac((isRight ? 1 : -1) * (user.getPosition() == Position.DOCTOR ? 2 : 1))
                .classify(AcRecord.VOTE)
                .reason((isRight ? "投票预测正确：" : "投票预测错误：") + title)
                .createTime(dateTime)
                .build();
    }

    /**
     * 根据论文最终结果计算投票者的ac
     * @param vote
     * @param result
     */
    public void computeVoteAc(Vote vote, boolean result, LocalDateTime dateTime) {
        // 1. 校验数据
        if (vote == null) {
            throw new RuntimeException("未发起投票");
        }

        Paper paper = vote.isExternal() ?
                externalPaperRepository.findByVid(vote.getId()) :
                internalPaperRepository.findByVid(vote.getId());
        if (paper == null) {
            throw new RuntimeException("投票对应的论文不存在，请联系开发者");
        }

        // 2. 获取 voteDetails
        List<VoteDetail> voteDetails = Optional.ofNullable(voteDetailRepository.listByVid(vote.getId()))
                .orElse(new ArrayList<>());

        // 3. 删除旧的 acRecord
        acRecordRepository.deleteAll(voteDetails.stream()
                .map(VoteDetail::getAcRecord)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );

        // 4. 生成新的 acRecord 更新 voteDetail
        voteDetails.forEach(voteDetail -> {
            voteDetail.setAcRecord(generateAcRecord(
                    paper.getTitle(),
                    voteDetail.getUser(),
                    voteDetail.getResult() == result,
                    dateTime));
        });
        voteDetailRepository.saveAll(voteDetails);

        // 5. 保存新的 acRecord
        acRecordRepository.saveAll(voteDetails.stream()
                .map(VoteDetail::getAcRecord)
                .collect(Collectors.toList())
        );

        // 6. 生成消息数据
        notifyService.voteAcMessage(vote.getId(), result);
    }

}
