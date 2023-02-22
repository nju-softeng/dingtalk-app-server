package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.dingApi.ReportApi;
import com.softeng.dingtalk.component.DateUtils;
import com.softeng.dingtalk.po_entity.User;
import com.softeng.dingtalk.dao.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
        // 获取指定日期一百条简报，做个初筛，避免一个人一下调用接口，导致服务被钉钉禁止
        var userids = reportApi.getSimpleReport(startTime, endTime, 100);
        log.info(startTime.toString() + "已经提交周报的userid: " + userids);
        return userRepository.listAllStudent().stream()
                .filter(user -> !userids.contains(user.getUserid())
                        // 实验室不会有超过一百个人的活跃度，只有没有提交日报的人，不会短路执行，api调用次数小
                        && !hasSubmittedWeeklyReport(user.getUserid(), startTime, endTime))
                .collect(Collectors.toList());
    }

}
