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
        try {
            // 确保线程安全问题
            lock.lock();
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
        String title = internalPaperRepository.getPaperTitleById(paperId);
        List<String> nameList = paperDetailRepository.listPaperAuthor(paperId);

        String markdown = String.format(VOTE_INFO_MARKDOWN, title, nameList.toString(), endTime.toString());
        String url = String.format(VOTE_INFO_URL, paperId);
        messageApi.sendActionCard("内部评审投票", markdown, "前往投票", url);
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
     * @param v
     * @return
     */
    @CacheEvict(value = "voting", allEntries = true)
    public Vote updateVote(Vote v) {
        log.debug("投票结果更新，清空缓存");
        int accept = voteDetailRepository.getAcceptCnt(v.getId());
        int total = voteDetailRepository.getCnt(v.getId());

        v.setStatus(true);
        v.setAccept(accept);
        v.setTotal(total);
        boolean result = accept > total - accept;
        v.setResult(result);
        voteRepository.save(v);
        
        if (!v.isExternal()) {
            // 如果是内部评审投票
            if (result == false) {
                internalPaperRepository.updatePaperResult(v.getPid(), InternalPaper.NOTPASS);
            } else {
                internalPaperRepository.updatePaperResult(v.getPid(), InternalPaper.REVIEWING);
            }
        }

        Map map = Map.of("vid", v.getId(), "isEnd", true);

        try {
            WebSocketController.sendInfo(objectMapper.writeValueAsString(map));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
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
        if (!vote.isExternal()) {
            Set<Integer> authorids = paperService.listAuthorId(vote.getPid());
            if (authorids.contains(Integer.valueOf(uid))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "论文作者不能参与投票！");
            }
        }


        if (now.isBefore(vote.getEndTime())) {
            // 投票未截止
            voteDetailRepository.save(voteDetail);
        } else {
            // 投票已截止
            throw new ResponseStatusException(HttpStatus.CONFLICT, "投票已经截止！");
        }

        return getVotingDetails(vid, uid);
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
        // myVoteMsg 我的投票对应的字符串，in (“accept”, “reject”, “unvote”)
        String myVoteMsg = myVote == null ? "unvote" : (myVote ? "accept" : "reject");
        // isOver 投票是否结束
        Boolean isOver = vote.getEndTime().isBefore(LocalDateTime.now());

        // acceptlist accept人员
        List<User> acceptUserList = new ArrayList<>();
        // rejectlist reject人员
        List<User> rejectUserList = new ArrayList<>();
        // unVoteNames 未投票人员的名单
        List<String> unVoteNames  = new ArrayList<>();
        // accept 票数
        int accept  = 0;
        // reject 票数
        int reject  = 0;
        // total 总投票人数
        int total   = 0;
        //
        double acceptWeights = 0.0;
        double rejectWeights = 0.0;
        if(isOver) {
            // 投票已结束
            acceptUserList  = voteDetailRepository.listAcceptUserlist(vid);
            rejectUserList  = voteDetailRepository.listRejectUserlist(vid);
            unVoteNames     = voteDetailRepository.findUnVoteUsername(vid);

            acceptWeights = acceptUserList.stream().mapToDouble(user -> {
                switch (user.getPosition()) {
                    case DOCTOR:
                        return 2.0;
                    case POSTGRADUATE:
                        return 1.0;
                    default:
                        return 0.0;
                }
            }).sum();
            rejectWeights = rejectUserList.stream().mapToDouble(user -> {
                switch (user.getPosition()) {
                    case DOCTOR:
                        return 2.0;
                    case POSTGRADUATE:
                        return 1.0;
                    default:
                        return 0.0;
                }
            }).sum();

            accept = acceptUserList.size();
            reject = rejectUserList.size();
            total  = accept + reject;
        }
        return Map.of(
                "vid",                vid,
                "status",             isOver,
                "accept",             accept,
                "total",              total,
                "reject",             reject,
                "myvote",             myVoteMsg,
                "acceptnames",        acceptUserList.stream().map(User::getName).collect(Collectors.toList()),
                "rejectnames",        rejectUserList.stream().map(User::getName).collect(Collectors.toList()),
                "unvotenames",        unVoteNames,
                "acceptedPercentage", acceptWeights / (acceptWeights + rejectWeights)
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
                .ac((isRight ? 1 : -1) * (user.getPosition() == Position.POSTGRADUATE ? 1 : 2))
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
        if (vote == null) throw new RuntimeException("未发起投票");

        List<VoteDetail> voteDetails = voteDetailRepository.listByVid(vote.getId());

        // 删除旧的 acRecord
        List<AcRecord> oldAcRecords = Optional.ofNullable(voteDetails).orElse(new ArrayList<>()).stream()
                .filter(x -> x.getAcRecord() != null)
                .map(x -> x.getAcRecord())
                .collect(Collectors.toList());
        acRecordRepository.deleteAll(oldAcRecords);


        Paper paper = vote.isExternal() ? externalPaperRepository.findByVid(vote.getId()) : internalPaperRepository.findByVid(vote.getId());
        if (paper == null) throw new RuntimeException("投票对应的论文不存在，请联系开发者");

        // 重新计算投票者的 acRecord
        List<AcRecord> acRecords = new ArrayList<>();
        voteDetails.forEach(vd -> {
            AcRecord acRecord = generateAcRecord(paper.getTitle(), vd.getUser(), vd.getResult() == result, dateTime);
            vd.setAcRecord(acRecord);
            acRecords.add(acRecord);
        });

        // 将数据保存到数据库
        acRecordRepository.saveAll(acRecords);
        voteDetailRepository.saveAll(voteDetails);

        // 发送消息
        notifyService.voteAcMessage(vote.getId(), result);

        // 计算助研金
        voteDetails.forEach(vd -> performanceService.computeSalary(vd.getUser().getId() , dateTime.toLocalDate()));
    }

}
