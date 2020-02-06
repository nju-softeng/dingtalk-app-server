package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Vote;
import com.softeng.dingtalk.entity.VoteDetail;
import com.softeng.dingtalk.repository.VoteDetailRepository;
import com.softeng.dingtalk.repository.VoteRepository;
import com.softeng.dingtalk.vo.VoteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author zhanyeye
 * @description
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


    public void createVote(VoteVO voteVO) {
        voteRepository.save(new Vote(voteVO));
    }

    public void poll(VoteDetail voteDetail) {
        voteDetailRepository.save(voteDetail);
    }



}
