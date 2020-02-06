package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.VoteDetail;
import com.softeng.dingtalk.service.VoteService;
import com.softeng.dingtalk.vo.VoteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/vote")
    public void addvote(@RequestBody VoteVO voteVO) {
        voteService.createVote(voteVO);
    }

    @PostMapping("/vote/{id}")
    public void addpoll(@PathVariable int id, @RequestBody VoteDetail voteDetail) {
        voteService.poll(voteDetail);
    }





}
