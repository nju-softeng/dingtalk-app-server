package com.softeng.dingtalk;

import com.softeng.dingtalk.entity.Vote;
import com.softeng.dingtalk.mapper.DcRecordMapper;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.service.*;
import com.softeng.dingtalk.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author zhanyeye
 * @description
 * @create 12/29/2019 10:49 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class TestIdea {

    @Autowired
    SubsidyLevelRepository subsidyLevelRepository;
    @Autowired
    SystemService systemService;
    @Autowired
    DcRecordMapper dcRecordMapper;

    @Autowired
    VoteRepository voteRepository;


    @Test
    public void test() {
        List<Vote> kk = voteRepository.listUpcomingVote(LocalDateTime.now());

    }



}
