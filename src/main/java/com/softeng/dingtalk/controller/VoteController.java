package com.softeng.dingtalk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.entity.InternalVote;
import com.softeng.dingtalk.entity.VoteDetail;
import com.softeng.dingtalk.service.PaperService;
import com.softeng.dingtalk.service.VoteService;
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


    /**
     * 创建一个投票
     * @param voteVO
     * @return
     */
    @PostMapping("/vote")
    public InternalVote addvote(@RequestBody VoteVO voteVO) {
        log.debug(voteVO.toString());
        return voteService.createVote(voteVO);
    }


    /**
     * 用户投票
     * @param vid
     * @param uid
     * @param voteDetail
     * @return
     * @throws IOException
     */
    @PostMapping("/vote/{vid}")
    public Map addpoll(@PathVariable int vid, @RequestAttribute int uid, @RequestBody VoteDetail voteDetail) throws IOException {
        // 标明这一票是谁投的
        voteDetail.setUser(new User(uid));
        Map map = voteService.poll(vid, uid, voteDetail);
        WebSocketController.sendInfo(objectMapper.writeValueAsString(map));
        return map;
    }


    /**
     * 获取投票详情
     * @param pid
     * @param uid
     * @return
     */
    @GetMapping("/vote/{pid}/detail")
    public Map getVoteDetail(@PathVariable int pid, @RequestAttribute int uid) {
        InternalVote vote = paperService.getVoteByPid(pid);
        if (vote.getDeadline().isBefore(LocalDateTime.now())) {
            //如果投票已经结束
            return voteService.getVotedDetail(vote.getId(), pid, uid);
        } else {
            //如果投票未结束
            return voteService.getVotingDetail(vote.getId(),uid);
        }
    }







}
