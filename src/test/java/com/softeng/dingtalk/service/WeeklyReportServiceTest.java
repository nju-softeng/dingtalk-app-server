package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.dingApi.ReportApi;
import com.softeng.dingtalk.dao.repository.AcRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;

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

    @Autowired
    AcRecordRepository acRecordRepository;

    @Test
    public void testHasSubmittedWeeklyReport() {
        Assert.assertEquals(weeklyReportService.hasSubmittedWeeklyReport("", LocalDateTime.of(2021, 10, 1, 0, 0), LocalDateTime.of(2021, 10, 3, 0, 0, 0)), false);
    }

    @Test
    public void testQueryUnSubmittedWeeklyReportUser() {
        Arrays.asList(
                LocalDateTime.of(2021, 11, 29, 0, 0, 0),
                LocalDateTime.of(2021, 12, 6, 0, 0, 0),
                LocalDateTime.of(2021, 12, 13, 0, 0, 0),
                LocalDateTime.of(2021, 12, 20, 0, 0, 0),
                LocalDateTime.of(2021, 12, 27, 0, 0, 0),
                LocalDateTime.of(2022, 1, 3, 0, 0, 0)
        ).forEach(startTime -> {
            var endTime = startTime.plusDays(7);
            var reason = String.format(
                    "%s 未按时提交周报",
                    endTime.minusDays(1).toLocalDate().toString()
            );
            log.info(reason);
            var users = weeklyReportService.queryUnSubmittedWeeklyReportUser(
                    endTime.minusDays(1),
                    endTime
            );
            users.forEach(user -> {log.info(user.getName());});

//            // 批量修改数据库
//            acRecordRepository.saveAll(
//                    users.stream()
//                            .map(user -> AcRecord.builder()
//                                    .user(user)
//                                    .ac(AcAlgorithm.getPointOfUnsubmittedWeekReport(user))
//                                    .classify(AcRecord.NORMAL)
//                                    .reason(reason)
//                                    .createTime(endTime)
//                                    .build())
//                            .collect(Collectors.toList())
//            );
        });


    }

}
