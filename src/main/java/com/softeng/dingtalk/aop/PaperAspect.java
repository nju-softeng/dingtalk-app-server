package com.softeng.dingtalk.aop;

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

/**
 * @author zhanyeye
 * @description
 * @create 3/12/2020 1:55 PM
 */
@Slf4j
@Component
@Aspect
public class PaperAspect {
    @Autowired
    NotifyService notifyService;
    @Autowired
    PerformanceService performanceService;


    // 论文出结果后更新助研金
    @AfterReturning(value = "execution(* com.softeng.dingtalk.service.PaperService.updateResult(..))", returning = "retVal")
    public void AfterPaperUpdateResult(JoinPoint joinPoint, Object retVal) throws Throwable {
        log.debug("计算论文ac后 发送消息， 更新绩效");

        // 发送消息
        Object[] args = joinPoint.getArgs();
        notifyService.paperAcMessage((int) args[0], (boolean) args[1]);
        // 计算助研金
        LocalDate date = LocalDate.now();
        int yearmonth = date.getYear() * 100 + date.getMonthValue();
        for (Integer id : (List<Integer>)retVal) {
            performanceService.computeSalary(id, yearmonth);
        }
    }


    // 投票结果更新助研金
    @AfterReturning(value = "execution(* com.softeng.dingtalk.service.VoteService.computeVoteAc(..))", returning = "retVal")
    public void AfterVoteUpdateResult(JoinPoint joinPoint, Object retVal) throws Throwable {
        log.debug("计算投票ac后，更新绩效，发送消息");

        // 发送消息
        Object[] args = joinPoint.getArgs();
        notifyService.voteAcMessage((int) args[0], (boolean) args[1]);
        // 计算助研金
        LocalDate date = LocalDate.now();
        int yearmonth = date.getYear() * 100 + date.getMonthValue();
        for (Integer id : (List<Integer>)retVal) {
            performanceService.computeSalary(id, yearmonth);
        }
    }


}
