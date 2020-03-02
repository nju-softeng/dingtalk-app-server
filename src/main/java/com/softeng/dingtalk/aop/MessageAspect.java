package com.softeng.dingtalk.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author zhanyeye
 * @description
 * @create 3/2/2020 9:54 AM
 */

@Slf4j
@Component
@Aspect
public class MessageAspect {
    @After("execution(* com.softeng.dingtalk.service.AuditService.addAuditResult(..))")
    public void addDcResultMessage(JoinPoint joinPoint) throws Throwable {
        log.debug("切面！！！！！！！！！！！！！！");
        Object[] args = joinPoint.getArgs();
        log.debug(args[0].toString());
    }

    @After("execution(* com.softeng.dingtalk.service.AuditService.updateAudit(..))")
    public void updateDcResultMessage(JoinPoint joinPoint) throws Throwable {
        log.debug("切面！！！！！！！！！！！！！！");
        Object[] args = joinPoint.getArgs();
        log.debug(args[0].toString());
    }
}
