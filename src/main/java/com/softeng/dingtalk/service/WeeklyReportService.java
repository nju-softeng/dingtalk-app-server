package com.softeng.dingtalk.service;

import com.softeng.dingtalk.api.ReportApi;
import com.softeng.dingtalk.component.DateUtils;
import com.softeng.dingtalk.repository.UserRepository;
import com.softeng.dingtalk.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhanyeye
 * @Description
 * @Date 19/10/2021
 **/
@Service
@Transactional
@Slf4j
public class WeeklyReportService {
    @Autowired
    ReportApi reportApi;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DateUtils dateUtils;

    public boolean hasSubmittedWeeklyReport(String userid, LocalDateTime startTime, LocalDateTime endTime) {
        return reportApi.getReport(userid, startTime, endTime).size() == 0 ? false : true;
    }

//    public void queryUnsubmittedWeeklyReportUser(YearMonth yearMonth) {
//        // 所有可用用户
//        List<UserVO> userVOS = userRepository.listUserVOS();
//        for (int i = 0; i < 5; i++) {
//            List<UserVO> tmpUserList = new ArrayList<>(userVOS);
//            LocalDate[] during = dateUtils.getWeekBeginAndStart(yearMonth, i);
//            if (during == null) continue;
//            for (UserVO user : userVOS) {
//            }
//        }
//    }

}
