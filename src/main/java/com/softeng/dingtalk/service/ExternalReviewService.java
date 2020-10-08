package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.ExternalVote;
import com.softeng.dingtalk.repository.ExternalVoteDetailRepository;
import com.softeng.dingtalk.repository.ExternalVoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: zhanyeye
 * @create: 2020-10-08 18:06
 **/
@Service
@Transactional
@Slf4j
public class ExternalReviewService {
    @Autowired
    ExternalVoteRepository externalVoteRepository;
    @Autowired
    ExternalVoteDetailRepository externalVoteDetailRepository;

    public void addExternalVote(ExternalVote externalVote) {
        externalVoteRepository.save(externalVote);
    }

    public void updateExternalVote(ExternalVote externalVote) {

    }

    public void deleteExternalVote() {

    }

    public List<ExternalVote> listExternalVote() {
        return externalVoteRepository.findAll();
    }

}
