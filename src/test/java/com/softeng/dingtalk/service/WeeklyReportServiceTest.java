package com.softeng.dingtalk.service;

import com.softeng.dingtalk.api.ReportApi;
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
    ReportApi reportApi;

    @Autowired
    WeeklyReportService weeklyReportService;

    @Test
    public void testHasSubmittedWeeklyReport() {
        Assert.assertEquals(weeklyReportService.hasSubmittedWeeklyReport("", LocalDateTime.of(2021, 10, 1, 0, 0), LocalDateTime.of(2021, 10, 3, 0, 0, 0)), false);
    }

    @Test
    public void testQueryUnsubmittedWeeklyReportUser() {
        // 1. 10.11 - 10.17 未按时交周报的同学
        System.out.println("10.11 - 10.17 未按时交周报的同学");
        var users = weeklyReportService.queryUnsubmittedWeeklyReportUser(
                LocalDateTime.of(2021, 10, 11, 0, 0, 0),
                LocalDateTime.of(2021, 10, 18, 0, 0, 0));
        for(var user : users) {
            System.out.println(user.getName());
        }
        // 2. 10.18 - 10.24 未按时交周报的同学
        System.out.println("10.18 - 10.24 未按时交周报的同学");
        users = weeklyReportService.queryUnsubmittedWeeklyReportUser(
                LocalDateTime.of(2021, 10, 18, 0, 0, 0),
                LocalDateTime.of(2021, 10, 25, 0, 0, 0));
        for(var user : users) {
            System.out.println(user.getName());
        }
        // 2. 10.25 - 10.31 未按时交周报的同学
        System.out.println("10.25 - 10.31 未按时交周报的同学");
        users = weeklyReportService.queryUnsubmittedWeeklyReportUser(
                LocalDateTime.of(2021, 10, 25, 0, 0, 0),
                LocalDateTime.of(2021, 11, 1, 0, 0, 0));
        for(var user : users) {
            System.out.println(user.getName());
        }
    }

//    @Test
//    public boolean testQueryUnsubmittedWeeklyReportUser(LocalDate date) {
//
////        weeklyReportService.
//    }
}
