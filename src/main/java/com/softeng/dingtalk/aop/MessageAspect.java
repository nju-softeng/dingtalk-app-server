package com.softeng.dingtalk.aop;


import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
    @AfterReturning(value = "execution(* com.softeng.dingtalk.service.AuditService.addAuditResult(..))", returning = "retVal")
    public void addDcResultMessage(JoinPoint joinPoint, Object retVal) throws Throwable {
        notifyService.reviewDcMessage((DcRecord) retVal);
    }


    // 审核人更新后发送消息
    @AfterReturning(value = "execution(* com.softeng.dingtalk.service.AuditService.updateAudit(..))", returning = "retVal")
    public void updateDcResultMessage(JoinPoint joinPoint, Object retVal) throws Throwable {
        notifyService.updateDcMessage((DcRecord) retVal);
    }


    // 论文结果确定后通知论文作者
    @AfterReturning(value = "execution(* com.softeng.dingtalk.service.PaperService.updateResult(..))")
    public void paperResultMessage(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        notifyService.paperAcMessage((int) args[0], (boolean) args[1]);
    }


    // 论文结果确定后通知投票者AC变化
    @AfterReturning("execution(* com.softeng.dingtalk.service.VoteService.computeVoteAc(..))")
    public void voteResultMessage(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        notifyService.voteAcMessage((int) args[0], (boolean) args[1]);
    }


    // 审核人依据公式设置 通知
    @AfterReturning(value = "execution(* com.softeng.dingtalk.service.ProjectService.autoSetProjectAc(..))", returning = "retVal")
    public void autoSetProjectAcMessage(JoinPoint joinPoint, Object retVal) throws Throwable {
        notifyService.autoSetProjectAcMessage((List<AcRecord>) retVal);
    }


    // 审核人依据公式设置 通知
    @AfterReturning(value = "execution(* com.softeng.dingtalk.service.ProjectService.manualSetProjectAc(..))", returning = "retVal")
    public void manualSetProjectAcMessage(JoinPoint joinPoint, Object retVal) throws Throwable {
        notifyService.manualSetProjectAcMessage((List<AcRecord>) retVal);
    }


}
