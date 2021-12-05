package com.softeng.dingtalk.service;

import com.softeng.dingtalk.api.ReportApi;
import com.softeng.dingtalk.component.DateUtils;
import com.softeng.dingtalk.entity.User;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * 查询用户在某时间区间内是否提交周报
     * @param userid 用户的钉钉id
     * @param startTime 收集周报的起始时间
     * @param endTime 收集周报的结束时间
     * @return
     */
    public boolean hasSubmittedWeeklyReport(String userid, LocalDateTime startTime, LocalDateTime endTime) {
        return reportApi.getReport(userid, startTime, endTime).size() != 0;
    }

    public List<User> queryUnSubmittedWeeklyReportUser(LocalDateTime startTime, LocalDateTime endTime) {
        return Optional.ofNullable(userRepository.listAllStudent()).orElse(new ArrayList<>()).stream()
                .filter(user -> !hasSubmittedWeeklyReport(user.getUserid(), startTime, endTime))
                .collect(Collectors.toList());
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
