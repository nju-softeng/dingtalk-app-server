package com.softeng.dingtalk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.entity.Vote;
import com.softeng.dingtalk.entity.VoteDetail;
import com.softeng.dingtalk.repository.VoteRepository;
import com.softeng.dingtalk.service.PaperService;
import com.softeng.dingtalk.service.VoteService;
import com.softeng.dingtalk.vo.PollVO;
import com.softeng.dingtalk.vo.VoteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 5:27 PM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class VoteController {
    @Autowired
    VoteService voteService;
    @Autowired
    PaperService paperService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VoteRepository voteRepository;



    /**
     * 创建一个投票
     * @param voteVO
     * @return
     */
    @PostMapping("/vote")
    public Vote addvote(@RequestBody VoteVO voteVO) {
        log.debug(voteVO.toString());
        return voteService.createVote(voteVO);
    }


    /**
     * 用户投票
     * @param vid
     * @param uid
     * @param vo
     * @return
     * @throws IOException
     */
    @PostMapping("/vote/{vid}")
    public Map addpoll(@PathVariable int vid, @RequestAttribute int uid, @RequestBody PollVO vo) throws IOException {
        VoteDetail voteDetail = new VoteDetail(new Vote(vo.getVid()), vo.getResult(), new User(uid));
        Map map = voteService.poll(vid, uid, voteDetail);
        WebSocketController.sendInfo(objectMapper.writeValueAsString(map));
        return map;
    }


    /**
     * 根据paperid获取投票详情
     * @param pid
     * @param uid
     * @return
     */
    @GetMapping("/vote/paper/{pid}/detail")
    public Map getVoteDetailByPid(@PathVariable int pid, @RequestAttribute int uid) {
        Vote vote = paperService.getVoteByPid(pid);
        if (vote.getEndTime().isBefore(LocalDateTime.now())) {
            //如果投票已经结束
            return voteService.getVotedDetail(vote.getId(), uid, vote.isExternal());
        } else {
            //如果投票未结束
            return voteService.getVotingDetail(vote.getId(), uid);
        }
    }


    /**
     * 根据voteid获取投票详情
     * @param pid
     * @param uid
     * @return
     */
    @GetMapping("/vote/{vid}/detail")
    public Map getVoteDetailByVid(@PathVariable int vid, @RequestAttribute int uid) {
        Vote vote = voteRepository.findById(vid).get();
        if (vote.getEndTime().isBefore(LocalDateTime.now())) {
            //如果投票已经结束
            return voteService.getVotedDetail(vid, uid, vote.isExternal());
        } else {
            //如果投票未结束
            return voteService.getVotingDetail(vid, uid);
        }
    }







}
