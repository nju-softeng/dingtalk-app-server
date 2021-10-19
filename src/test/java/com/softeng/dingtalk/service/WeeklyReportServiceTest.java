package com.softeng.dingtalk.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author zhanyeye
 * @Description
 * @Date 19/10/2021
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class WeeklyReportServiceTest {

    @Autowired
    WeeklyReportService weeklyReportService;

    @Test
    public void testHasSubmittedWeeklyReport() {
        Assert.assertEquals(weeklyReportService.hasSubmittedWeeklyReport("", LocalDateTime.of(2021, 10, 0, 0, 0), LocalDateTime.of(2021, 10, 3, 0, 0, 0)), false);
    }

//    @Test
//    public boolean testQueryUnsubmittedWeeklyReportUser(LocalDate date) {
//
////        weeklyReportService.
//    }
}
