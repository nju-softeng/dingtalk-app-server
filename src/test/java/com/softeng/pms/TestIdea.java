package com.softeng.pms;

import com.softeng.pms.entity.Review;
import com.softeng.pms.mapper.DcRecordMapper;
import com.softeng.pms.repository.*;
import com.softeng.pms.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    @Autowired
    PaperService paperService;


    @Test
    public void test() {
//        List<Vote> kk = voteRepository.listUpcomingVote(LocalDateTime.now());
        List<Review> aa = paperService.listReview(1, true);

    }



}
