package com.softeng.pms.service;

import com.softeng.pms.repository.DcSummaryRepository;
import com.softeng.pms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanyeye
 * @description
 * @create 1/29/2020 2:36 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class PerformanceServiceTest {
    @Autowired
    DcSummaryRepository dcSummaryRepository;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SystemService systemService;

    @Test
    public void test1() {
        Double d =  dcSummaryRepository.findTopup(1, 202006);
        log.debug(d.toString());
    }



}
