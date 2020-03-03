package com.softeng.dingtalk.aop;


import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhanyeye
 * @description  绩效变更时发送消息
 * @create 3/2/2020 9:54 AM
 */
@Slf4j
@Component
@Aspect
public class MessageAspect {

    @Autowired
    NotifyService notifyService;


    @Around("execution(* com.softeng.dingtalk.service.AuditService.addAuditResult(..))")
    public Object addDcResultMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object object = joinPoint.proceed();
        notifyService.reviewDcMessage((DcRecord) object);
        return object;
    }

    @Around("execution(* com.softeng.dingtalk.service.AuditService.updateAudit(..))")
    public Object updateDcResultMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object object = joinPoint.proceed();
        notifyService.updateDcMessage((DcRecord) object);
        return object;
    }

    @After("execution(* com.softeng.dingtalk.service.PaperService.updateResult(..))")
    public void paperResultMessage(JoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

    }

}
