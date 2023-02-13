package com.softeng.dingtalk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeng.dingtalk.controller.WebSocketController;
import com.softeng.dingtalk.api.MessageApi;
import com.softeng.dingtalk.dao.repository.*;
import com.softeng.dingtalk.po.*;

import com.softeng.dingtalk.enums.Position;
import com.softeng.dingtalk.vo.VoteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

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

    @Value("${paper.flatRateNumerator}")
    private double flatRateNumerator;
    @Value("${paper.flatRateDenominator}")
    private double flatRateDenominator;

    /**
     * 创建论文评审投票
     *
     * @param vo
     * @return
     */
    @CacheEvict(value = "voting", allEntries = true)
    public VotePo createVote(VoteVO vo) {
        lock.lock();
        try {
            // 确保线程安全问题
            if (voteRepository.isExisted(vo.getPaperid(), false) == 0) {
                // 如果投票还没有被创建
                VotePo votePo = new VotePo(LocalDateTime.now(), vo.getEndTime(), vo.getPaperid());
                voteRepository.save(votePo);
                internalPaperRepository.updatePaperVote(vo.getPaperid(), votePo.getId());
                sendVoteInfoCardToDingtalk(vo.getPaperid(), vo.getEndTime().toLocalTime());
                return voteRepository.refresh(votePo);
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "慢了一步，投票已经被别人发起了");
            }
        } finally {
            lock.unlock();
        }
    }


    /**
     * 向钉钉群中发送投票消息卡片
     *
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
     * @param now
     * @return
     */
    public List<VotePo> listUpcomingVote(LocalDateTime now) {
        return voteRepository.listUpcomingVote(now);
    }


    /**
     * 查询没有结束的投票,但即将结束的投票
     * 缓存未结束的投票，用于减少查询数据的次数，当创建新投票后要清空缓存
     *
     * @param now
     * @return
     */
    @Cacheable(value = "voting")
    public List<VotePo> listUnderwayVote(LocalDateTime now) {
        return voteRepository.listClosingVote(now);
    }


    /**
     * 更新投票的最终结果，投票截止后调用, 被定时器调用
     *
     * @param votePo
     * @return
     */
    @CacheEvict(value = "voting", allEntries = true)
    public VotePo updateVote(VotePo votePo) {
        log.debug("投票结果更新，清空缓存");
        votePo.setStatus(true);
        votePo.setAccept(voteDetailRepository.getAcceptCnt(votePo.getId()));
        votePo.setTotal(voteDetailRepository.getCnt(votePo.getId()));
        votePo.setResult(getVotingResult(votePo));
        voteRepository.save(votePo);

        // 如果是内部评审投票
        if (!votePo.isExternal()) {
            internalPaperRepository.updatePaperResult(
                    votePo.getPid(),
                    votePo.getResult() == 1 ? InternalPaperPo.REVIEWING : (votePo.getResult() == 2 ? InternalPaperPo.FLAT : InternalPaperPo.NOTPASS)
                    );
        }

        try {
            WebSocketController.sendInfo(objectMapper.writeValueAsString(Map.of(
                    "vid", votePo.getId(),
                    "isEnd", true
            )));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return votePo;
    }


    /**
     * 用户投票
     *
     * @param vid
     * @param uid
     * @param voteDetailPo
     * @return
     */
    public Map poll(int vid, int uid, VoteDetailPo voteDetailPo) {
        // 收到投票的时间
        LocalDateTime now = LocalDateTime.now();
        VotePo votePo = voteRepository.findById(vid).get();

        // 如果是内部评审论文，判断投票人是否为论文作者
        if (paperService.listAuthorId(votePo.getPid()).contains(uid)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "论文作者不能参与投票！");
        }

        // 投票已经截止
        if (now.isAfter(votePo.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "投票已经截止！");
        }

        voteDetailRepository.save(voteDetailPo);
        return getVotingDetails(vid, uid);
    }

    /**
     * 不同的职位对应不同的票权
     *
     * @param userPo 用户
     * @return 票权值
     */
    public static double getWeight(UserPo userPo) {
        switch (userPo.getPosition()) {
            case DOCTOR:
                return 2.0;
            case ACADEMIC:
            case PROFESSIONAL:
                return 1.0;
            default:
                return 0.0;
        }
    }

    /**
     * 根据投接受票的投拒绝票的人加权后算出接受的百分比值
     *
     * @param acceptUserListPo 投接受的人的列表
     * @param rejectUserListPo 投拒绝的人的列表
     * @return [0, 1)
     */
    public double calculatePercentageOfVotesAccepted(List<UserPo> acceptUserListPo, List<UserPo> rejectUserListPo) {
        double acceptWeights = acceptUserListPo.stream().mapToDouble(VoteService::getWeight).sum();
        double rejectWeights = rejectUserListPo.stream().mapToDouble(VoteService::getWeight).sum();
        double totleWeights = acceptWeights + rejectWeights;
        return totleWeights == 0.0 ? 0.0 : acceptWeights / totleWeights;
    }

    /**
     * 算出某次投票的接受的加权百分比
     *
     * @param votePo
     * @return [0, 1)
     */
    public double calculatePercentageOfVotesAccepted(VotePo votePo) {
        return calculatePercentageOfVotesAccepted(
                voteDetailRepository.listAcceptUserlist(votePo.getId()),
                voteDetailRepository.listRejectUserlist(votePo.getId())
        );
    }

    /**
     * 判断某次投票是否通过
     *
     * @param votePo
     * @return
     */
    public int getVotingResult(VotePo votePo) {
        double result = calculatePercentageOfVotesAccepted(votePo);
        if (result > flatRateNumerator/flatRateDenominator) {
            return 1;
        } else if (result == flatRateNumerator/flatRateDenominator) {
            return 2;
        } else {
            return 0;
        }
    }


    /**
     * 获取投票详情
     *
     * @param vid 某次投票的id
     * @param uid 用户id
     * @return map 待重构为具体对象
     */
    public Map getVotingDetails(int vid, int uid) {
        VotePo votePo = voteRepository.findById(vid).get();
        // myVote 我的投票，in (accept, reject, unvote)
        Boolean myVote = voteDetailRepository.getVoteDetail(vid, uid);
        // isOver 投票是否结束
        Boolean isOver = votePo.getEndTime().isBefore(LocalDateTime.now());

        List<UserPo> acceptUserListPo = new ArrayList<>();
        List<UserPo> rejectUserListPo = new ArrayList<>();
        List<String> unVoteNames = new ArrayList<>();

        double acceptedPercentage = 0.0;

        if (isOver) {
            // 投票已结束
            acceptUserListPo = voteDetailRepository.listAcceptUserlist(vid);
            rejectUserListPo = voteDetailRepository.listRejectUserlist(vid);
            unVoteNames = voteDetailRepository.findUnVoteUsername(vid);
            acceptedPercentage = calculatePercentageOfVotesAccepted(acceptUserListPo, rejectUserListPo);
        }
        return Map.of(
                "vid", vid,
                "status", isOver,
                "accept", acceptUserListPo.size(),
                "reject", rejectUserListPo.size(),
                "total", acceptUserListPo.size() + rejectUserListPo.size(),
                "myvote", myVote == null ? "unvote" : (myVote ? "accept" : "reject"),
                "acceptnames", acceptUserListPo.stream().map(UserPo::getName).collect(Collectors.toList()),
                "rejectnames", rejectUserListPo.stream().map(UserPo::getName).collect(Collectors.toList()),
                "unvotenames", unVoteNames,
                "acceptedPercentage", acceptedPercentage
        );
    }

    /**
     * 生成投票的ac记录
     *
     * @param title
     * @param userPo
     * @param voteDetail
     * @param finalResult
     * @param dateTime
     * @return
     */
    private AcRecordPo generateAcRecord(String title, UserPo userPo, boolean voteDetail, int finalResult, LocalDateTime dateTime) {
        int coefficient = 0;
        if (finalResult == InternalPaperPo.SUSPEND) {
            // 中止外投不扣分
            coefficient = 0;
        } else if (finalResult != 2) {
            if ((voteDetail && finalResult == 1) || (!voteDetail && finalResult == 0)) {
                coefficient = 1;
            } else {
                coefficient = -1;
            }
        }
        return AcRecordPo.builder()
                .user(userPo)
                // 论文投票AC变化，对于硕士生是1分，对于博士生是2分
                .ac(coefficient * (userPo.getPosition() == Position.DOCTOR ? 2 : 1))
                .classify(AcRecordPo.VOTE)
                .reason((coefficient == 0 ? "投稿中止：" : (coefficient == 1 ? "投票预测正确：" : "投票预测错误：") + title))
                .createTime(dateTime)
                .build();
    }

    /**
     * 根据论文最终结果计算投票者的ac
     *
     * @param votePo
     * @param result
     */
    public void computeVoteAc(VotePo votePo, int result, LocalDateTime dateTime) {
        // 1. 校验数据
        if (votePo == null) {
            throw new RuntimeException("未发起投票");
        }

        PaperPo paper = votePo.isExternal() ?
                externalPaperRepository.findByVid(votePo.getId()) :
                internalPaperRepository.findByVid(votePo.getId());
        if (paper == null) {
            throw new RuntimeException("投票对应的论文不存在，请联系开发者");
        }

        // 2. 获取 voteDetails
        List<VoteDetailPo> voteDetailPos = Optional.ofNullable(voteDetailRepository.listByVid(votePo.getId()))
                .orElse(new ArrayList<>());

        // 3. 删除旧的 acRecord
        acRecordRepository.deleteAll(voteDetailPos.stream()
                .map(VoteDetailPo::getAcRecordPO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );

        // 4. 生成新的 acRecord 更新 voteDetail
        voteDetailPos.forEach(voteDetailPo -> {
            voteDetailPo.setAcRecordPO(generateAcRecord(
                    paper.getTitle(),
                    voteDetailPo.getUser(),
                    voteDetailPo.isResult(),
                    result,
                    dateTime));
        });
        voteDetailRepository.saveAll(voteDetailPos);

        // 5. 保存新的 acRecord
        acRecordRepository.saveAll(voteDetailPos.stream()
                .map(VoteDetailPo::getAcRecordPO)
                .collect(Collectors.toList())
        );

        // 6. 生成消息数据
        notifyService.voteAcMessage(votePo.getId(), result);
    }

}
