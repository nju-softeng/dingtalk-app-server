package com.softeng.dingtalk.aop;

import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.service.NotifyService;
import com.softeng.dingtalk.service.PerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhanyeye
 * @description
 * @create 3/12/2020 1:55 PM
 */
@Slf4j
@Component
@Aspect
public class ProjectAspect {
    @Autowired
    NotifyService notifyService;
    @Autowired
    PerformanceService performanceService;


    // 审核人依据公式设置 通知
    @AfterReturning(value = "execution(* com.softeng.dingtalk.service.ProjectService.autoSetProjectAc(..))", returning = "retVal")
    public void AfterAutoSetProjectAc(JoinPoint joinPoint, Object retVal) throws Throwable {
        // 发送消息
        List<AcRecord> acRecords = (List<AcRecord>) retVal;
        notifyService.autoSetProjectAcMessage(acRecords);

        // 计算助研金
        LocalDate date = LocalDate.now();
        List<Integer> ids = acRecords.stream().map(x -> x.getUser().getId()).collect(Collectors.toList());
        int yearmonth = date.getYear() * 100 + date.getMonthValue();
        for (Integer id : ids) {
            performanceService.computeSalary(id, yearmonth);
        }

    }


    // 审核人依据公式设置 通知
    @AfterReturning(value = "execution(* com.softeng.dingtalk.service.ProjectService.manualSetProjectAc(..))", returning = "retVal")
    public void AfterManualSetProjectAc(JoinPoint joinPoint, Object retVal) throws Throwable {
        // 发送消息
        List<AcRecord> acRecords = (List<AcRecord>) retVal;
        notifyService.manualSetProjectAcMessage(acRecords);

        // 计算助研金
        LocalDate date = LocalDate.now();
        List<Integer> ids = acRecords.stream().map(x -> x.getUser().getId()).collect(Collectors.toList());
        int yearmonth = date.getYear() * 100 + date.getMonthValue();
        for (Integer id : ids) {
            performanceService.computeSalary(id, yearmonth);
        }
    }


}
