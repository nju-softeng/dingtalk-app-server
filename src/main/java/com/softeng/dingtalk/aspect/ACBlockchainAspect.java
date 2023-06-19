package com.softeng.dingtalk.aspect;


import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.dao.repository.AcRecordRepository;
import com.softeng.dingtalk.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ConditionalOnExpression("${blockchain.service.enable:true}")
@Aspect
@Component
@Slf4j
public class ACBlockchainAspect {
    @Autowired
    SystemService systemService;
    @Autowired
    AcRecordRepository acRecordRepository;
    boolean isCreate=false;
    //FabricManager manager=FabricManager.obtain();
    public static List timeCostList=new ArrayList<>();
    @Pointcut("execution(* com.softeng.dingtalk.dao.repository.AcRecordRepository.save(..))")
    public void saveRecord(){
    }

    @Pointcut("execution(* com.softeng.dingtalk.dao.repository.AcRecordRepository.deleteAll(..))")
    public void deleteRecordList(){
    }

    @Pointcut("execution(* com.softeng.dingtalk.dao.repository.AcRecordRepository.saveAll(..)) ||" +
            "execution(* com.softeng.dingtalk.dao.repository.AcRecordRepository.saveBatch(..))")
    public void saveRecordList(){
    }

    @AfterReturning("saveRecord()")
    public void afterSaveRecord(JoinPoint point){
        AcRecord param=(AcRecord) point.getArgs()[0];
//        String key=param.getId().toString();
//        String value=JSON.toJSONString(param);
        log.info("after:"+param.toString());
    }

    @AfterReturning("deleteRecordList()")
    public void afterDeleteRecordList(JoinPoint point){
        AcRecord param=(AcRecord) point.getArgs()[0];
//        String key=param.getId().toString();
//        String value=JSON.toJSONString(param);
        log.info("after:"+param.toString());
    }


}
