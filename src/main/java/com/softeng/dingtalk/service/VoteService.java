package com.softeng.dingtalk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.controller.WebSocketController;
import com.softeng.dingtalk.entity.*;

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

import java.util.*;
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
    PaperRepository paperRepository;
    @Autowired
    PaperDetailRepository paperDetailRepository;
    @Autowired
    DingTalkUtils dingTalkUtils;
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


    //--------------------------------------------
    //  内部论文评审相关操作
    //--------------------------------------------

    /**
     * 创建投票 (内部投票)
     * 钉钉发送消息
     * 同时清空清空未结束投票缓存
     * @param voteVO
     */
    @CacheEvict(value = "voting", allEntries = true)
    public Vote createVote(VoteVO voteVO) {

        // 判断投票是否已经创建过
        if (voteRepository.isExisted(voteVO.getPaperid(), false) != 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "慢了一步，投票已经被别人发起了");
        }

        Vote vote = new Vote(LocalDateTime.now() ,LocalDateTime.of(LocalDate.now(), voteVO.getEndTime()), voteVO.getPaperid());

        voteRepository.save(vote);
        paperRepository.updatePaperVote(voteVO.getPaperid(), vote.getId());
        // 发送投票信息
        String title = paperRepository.getPaperTitleById(voteVO.getPaperid());
        List<String> namelist = paperDetailRepository.listPaperAuthor(voteVO.getPaperid());


        StringBuilder markdown = new StringBuilder().append(" #### 投票 \n ##### 论文： ").append(title).append(" \n ##### 作者： ");
        for (String name : namelist) {
            markdown.append(name).append(", ");
        }
        markdown.append(" \n 截止时间: ").append(voteVO.getEndTime().toString());
        String url = new StringBuilder().append("/paper/in-detail/").append(voteVO.getPaperid()).append("/vote").toString();

        dingTalkUtils.sendActionCard("内部评审投票", markdown.toString(), "前往投票", url);

        return voteRepository.refresh(vote);
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
                paperRepository.updatePaperResult(v.getPid(), Paper.NOTPASS);
            } else {
                paperRepository.updatePaperResult(v.getPid(), Paper.REVIEWING);
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
            Set<Integer> authorids = paperService.listAuthorid(vote.getPid());
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

        // 返回当前的投票结果
        List<String> acceptlist =  voteDetailRepository.listAcceptNamelist(vid);
        List<String> rejectlist = voteDetailRepository.listRejectNamelist(vid);

        // accept 票数
        int accept = acceptlist.size();
        // reject 票数
        int reject = rejectlist.size();
        // 总投票数
        int total = accept + reject;
        return Map.of("vid", vid, "status", true,"accept", accept, "total", total, "reject", reject, "myresult", voteDetail.getResult(),"acceptnames",acceptlist,"rejectnames", rejectlist);
    }


    /**
     * 投票还未结束,用户获取投票信息
     * @param vid
     * @param uid
     * @return
     */
    public Map getVotingDetail(int vid, int uid){
        Boolean myresult = voteDetailRepository.getVoteDetail(vid, uid);
        if (myresult != null) {
            //用户已经投票，可以查看结果
            List<String> acceptlist =  voteDetailRepository.listAcceptNamelist(vid);
            List<String> rejectlist = voteDetailRepository.listRejectNamelist(vid);
            // accept 票数
            int accept = acceptlist.size();
            // reject 票数
            int reject = rejectlist.size();
            // 总投票数
            int total = accept + reject;

            return Map.of("vid", vid, "status", false,"accept", accept, "total", total, "reject", reject, "myresult", myresult, "acceptnames",acceptlist,"rejectnames", rejectlist);
        } else {
            //用户没有投票，不可以查看结果
            return Map.of("vid", vid, "status", false);
        }
    }


    /**
     * 获取已经结束的投票信息
     * @param vid
     * @param uid
     * @return
     */
    public Map getVotedDetail(int vid, int uid, boolean isExternal) {
        List<String> acceptlist = voteDetailRepository.listAcceptNamelist(vid);
        List<String> rejectlist = voteDetailRepository.listRejectNamelist(vid);
        // accept 票数
        int accept = acceptlist.size();
        // reject 票数
        int reject = rejectlist.size();
        // 总投票数
        int total = accept + reject;
        // 用户本人的投票情况：accept, reject, 未参与(null)
        Boolean myresult = voteDetailRepository.getVoteDetail(vid, uid);


        // 获取投票的截止时间
        LocalDateTime endTime = voteRepository.getEndTimeByVid(vid);

        // 未投票人员名单
        // 1. 查询所有不是待定且在投票之前加入的用户用户的id，已经投票的用户的id，
        Set<Integer> totalIds = userRepository.listStudentIdBeforeVoteTime(endTime);
        Set<Integer> votedIds = voteDetailRepository.findVoteUserid(vid);


        // 2. 减去所有投票用户和论文作者的id
        totalIds.removeAll(votedIds);

        if (isExternal == false) {
            Paper paper = paperRepository.findByVid(vid);
            Set<Integer> authorids = paperService.listAuthorid(paper.getId());
            totalIds.removeAll(authorids);
        }





        //totalIds.removeAll(alumniids);

        // 3. 通过为投票用户id集合去查询用户姓名
        Set<String> unVoteNames = new HashSet<>();
        if (totalIds.size() > 0) {
            unVoteNames = userRepository.listNameByids(totalIds);
        }

        if (myresult == null) {
            return Map.of("vid", vid, "status", true,"accept", accept, "total", total, "reject", reject, "acceptnames",acceptlist,"rejectnames", rejectlist, "unvotenames", unVoteNames);
        } else {
            return Map.of("vid", vid, "status", true,"accept", accept, "total", total, "reject", reject, "myresult", myresult, "acceptnames",acceptlist,"rejectnames", rejectlist, "unvotenames", unVoteNames);
        }

    }


    /**
     * 根据论文最终结果计算投票者的ac
     * @param vote
     * @param result
     */
    public void computeVoteAc(Vote vote, boolean result) {
        if (vote != null) {
            List<VoteDetail> voteDetails = voteDetailRepository.listByVid(vote.getId());
            List<AcRecord> acRecords = new ArrayList<>();

            List<AcRecord> oldAcRecord = Optional.ofNullable(voteDetails).map(List::stream).orElseGet(Stream::empty)
                    .filter(x -> x.getAcRecord() != null).map(x -> x.getAcRecord()).collect(Collectors.toList());
            // 删除旧的 acRecord
            acRecordRepository.deleteAll(oldAcRecord);
            String title = paperRepository.findByVid(vote.getId()).getTitle();

            for (VoteDetail vd : voteDetails) {
                AcRecord acRecord;
                if (vd.getResult() == result) {
                    acRecord = new AcRecord(vd.getUser(), 1, "投票预测正确：" + title, AcRecord.VOTE);
                } else {
                    acRecord = new AcRecord(vd.getUser(), -1, "投票预测错误：" + title, AcRecord.VOTE);
                }
                vd.setAcRecord(acRecord);
                acRecords.add(acRecord);
            }
            acRecordRepository.saveAll(acRecords);
            voteDetailRepository.saveAll(voteDetails);

            // 发送消息
            notifyService.voteAcMessage(vote.getId(), result);
            // 计算助研金
            LocalDate date = LocalDate.now();
            int yearmonth = date.getYear() * 100 + date.getMonthValue();
            for (VoteDetail vd : voteDetails) {
                performanceService.computeSalary(vd.getUser().getId() , yearmonth);
            }

        } else {
            throw new RuntimeException("未发起投票");
        }

    }


    //--------------------------------------------
    //  外部论文评审相关操作
    //--------------------------------------------



}
