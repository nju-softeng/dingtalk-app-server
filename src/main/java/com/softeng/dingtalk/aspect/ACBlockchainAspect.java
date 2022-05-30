package com.softeng.dingtalk.aspect;


import com.alibaba.fastjson.JSON;
import com.softeng.dingtalk.api.BlockChainApi;
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

    @Autowired
    BlockChainApi blockChainApi;

    @Pointcut("execution(* com.softeng.dingtalk.repository.AcRecordRepository.save(..))")
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
        blockChainApi.create(param);
        log.info("after:"+param.toString());
    }

    @AfterReturning("saveAllRecord()")
    public void afterSaveAllRecord(JoinPoint point) throws ProposalException, InvalidArgumentException {
       List<AcRecord> param=(List<AcRecord>) point.getArgs()[0];
        for (AcRecord acRecord : param) {
            blockChainApi.create(acRecord);
        }
    }

    @AfterReturning("deleteRecord()")
    public void afterDeleteRecord(JoinPoint point) throws ProposalException, InvalidArgumentException {
        AcRecord param=(AcRecord) point.getArgs()[0];
        blockChainApi.delete(param);
        log.info("after:"+param.toString());
    }

    @AfterReturning("deleteAllRecord()")
    public void afterDeleteAllRecord(JoinPoint point) throws ProposalException, InvalidArgumentException {
        List<AcRecord> param=(List<AcRecord>) point.getArgs()[0];
        for (AcRecord acRecord : param) {
            blockChainApi.delete(acRecord);
        }
    }

}
