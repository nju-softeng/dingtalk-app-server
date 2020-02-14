package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.entity.Vote;
import com.softeng.dingtalk.entity.VoteDetail;
import com.softeng.dingtalk.service.PaperService;
import com.softeng.dingtalk.service.VoteService;
import com.softeng.dingtalk.vo.VoteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/vote")
    public void addvote(@RequestBody VoteVO voteVO) {
        log.debug(voteVO.toString());
        voteService.createVote(voteVO);
    }

    @PostMapping("/vote/{id}")
    public void addpoll(@PathVariable int id, @RequestBody VoteDetail voteDetail) {

        voteDetail.setUser(new User(id));
        voteService.poll(voteDetail);
    }

    //获取投票详情
    @GetMapping("/vote/{pid}/detail")
    public Map getVoteDetail(@PathVariable int pid, @RequestAttribute int uid) {
        Vote vote = paperService.getVoteByPid(pid);
        if (vote.isStatus()) { //如果投票已经结束
            return voteService.getVoteDetail(vote.getId());
        } else { //如果投票未结束
            return voteService.getVoteDetail(vote.getId(),uid);
        }
    }




}
