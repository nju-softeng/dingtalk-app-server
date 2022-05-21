package com.softeng.dingtalk.aspect;


import com.alibaba.fastjson.JSON;
import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.fabric.FabricManager;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    @Pointcut("execution(* com.softeng.dingtalk.repository.AcRecordRepository.save(..))")
    public void saveRecord(){
    }

    @Pointcut("execution(* com.softeng.dingtalk.repository.AcRecordRepository.deleteAll(..))")
    public void deleteRecordList(){
    }

    @Pointcut("execution(* com.softeng.dingtalk.repository.AcRecordRepository.saveAll(..)) ||" +
            "execution(* com.softeng.dingtalk.repository.AcRecordRepository.saveBatch(..))")
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
