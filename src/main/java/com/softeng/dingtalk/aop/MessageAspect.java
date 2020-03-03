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


    // 审核人审核后发送消息
    @Around("execution(* com.softeng.dingtalk.service.AuditService.addAuditResult(..))")
    public Object addDcResultMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object object = joinPoint.proceed();
        notifyService.reviewDcMessage((DcRecord) object);
        return object;
    }

    // 审核人更新后发送消息
    @Around("execution(* com.softeng.dingtalk.service.AuditService.updateAudit(..))")
    public Object updateDcResultMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object object = joinPoint.proceed();
        notifyService.updateDcMessage((DcRecord) object);
        return object;
    }

    // 论文结果确定后通知论文作者
    @After("execution(* com.softeng.dingtalk.service.PaperService.updateResult(..))")
    public void paperResultMessage(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        notifyService.paperAcMessage((int) args[0], (boolean) args[1]);
    }

    // 论文结果确定后通知投票者AC变化
    @After("execution(* com.softeng.dingtalk.service.VoteService.computeVoteAc(..))")
    public void voteResultMessage(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        notifyService.voteAcMessage((int) args[0], (boolean) args[1]);
    }





}
