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
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@Aspect
@Component
@Slf4j
public class ACBlockchainAspect {
    @Autowired
    SystemService systemService;
    @Autowired
    AcRecordRepository acRecordRepository;

    FabricManager manager=FabricManager.obtain();

    @Pointcut("execution(* com.softeng.dingtalk.controller.PaperController.getPaper(..))")
    public void saveRecord(){
    }

    @Pointcut("execution(* com.softeng.dingtalk.repository.AcRecordRepository.saveAll(..))")
    public void saveAllRecord(){
    }

    @Pointcut("execution(* com.softeng.dingtalk.repository.AcRecordRepository.delete(..))")
    public void deleteRecord(){
    }

    @Pointcut("execution(* com.softeng.dingtalk.repository.AcRecordRepository.deleteAll(..))")
    public void deleteAllRecord(){
    }

    @AfterReturning("saveRecord()")
    public void afterSaveRecord(JoinPoint point) throws ProposalException, InvalidArgumentException {
        AcRecord param=(AcRecord) point.getArgs()[0];
        String key=param.getId().toString();
        String value=JSON.toJSONString(param);
        manager.getManager().invoke("create", new String[]{key, value});
        log.info("after:"+param.toString());
    }

    @AfterReturning("saveRecord()")
    public void afterSaveAllRecord(JoinPoint point) throws ProposalException, InvalidArgumentException {
        AcRecord[] param=(AcRecord[]) point.getArgs()[0];
        for (AcRecord acRecord : param) {
            String key = acRecord.getId().toString();
            String value = JSON.toJSONString(acRecord);
            manager.getManager().invoke("create", new String[]{key, value});
        }
        log.info("after:"+ Arrays.toString(param));
    }

    @AfterReturning("deleteRecord()")
    public void afterDeleteRecord(JoinPoint point) throws ProposalException, InvalidArgumentException {
        AcRecord param=(AcRecord) point.getArgs()[0];
        String key=param.getId().toString();
        manager.getManager().invoke("delete", new String[]{key});
        log.info("after:"+param.toString());
    }

    @AfterReturning("deleteRecord()")
    public void afterDeleteAllRecord(JoinPoint point) throws ProposalException, InvalidArgumentException {
        AcRecord[] param=(AcRecord[]) point.getArgs()[0];
        for (AcRecord acRecord : param) {
            String key = acRecord.getId().toString();
            manager.getManager().invoke("delete", new String[]{key});
        }
        log.info("after:"+ Arrays.toString(param));
    }

}
