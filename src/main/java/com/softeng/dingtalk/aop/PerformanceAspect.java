package com.softeng.dingtalk.aop;

import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.service.PerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhanyeye
 * @description DC或AC更新时触发，更新绩效汇总，触发助研金
 * @create 3/2/2020 3:00 PM
 */
@Slf4j
@Component
@Aspect
public class PerformanceAspect {

    @Autowired
    PerformanceService performanceService;

    @After("execution(* com.softeng.dingtalk.service.AuditService.updateDcSummary(..))")
    public void computeSalary(JoinPoint joinPoint) throws Throwable {
        log.debug("切面computeSalary");
        Object[] args = joinPoint.getArgs();
        DcRecord dc = (DcRecord) args[0];
        performanceService.computeSalary(dc.getApplicant().getId(), dc.getYearmonth());
    }
}
