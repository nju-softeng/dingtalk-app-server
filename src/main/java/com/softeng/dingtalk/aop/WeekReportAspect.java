package com.softeng.dingtalk.aop;

import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.service.NotifyService;
import com.softeng.dingtalk.service.PerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhanyeye
 * @description
 * @create 3/12/2020 1:55 PM
 */
@Slf4j
@Component
@Aspect
public class WeekReportAspect {
    @Autowired
    NotifyService notifyService;
    @Autowired
    PerformanceService performanceService;


    // 审核人审核后发送消息
    @AfterReturning(value = "execution(* com.softeng.dingtalk.service.AuditService.addAuditResult(..))", returning = "retVal")
    public void addDcResultMessage(JoinPoint joinPoint, Object retVal) throws Throwable {
        DcRecord dc = (DcRecord) retVal;
        notifyService.reviewDcMessage(dc);
        performanceService.computeSalary(dc.getApplicant().getId(), dc.getYearmonth());
    }


    // 审核人更新后发送消息
    @AfterReturning(value = "execution(* com.softeng.dingtalk.service.AuditService.updateAudit(..))", returning = "retVal")
    public void updateDcResultMessage(JoinPoint joinPoint, Object retVal) throws Throwable {
        DcRecord dc = (DcRecord) retVal;
        notifyService.updateDcMessage(dc);
        performanceService.computeSalary(dc.getApplicant().getId(), dc.getYearmonth());
    }

}
