package com.softeng.dingtalk.aspect;

import com.softeng.dingtalk.dao.repository.AcRecordRepository;
import com.softeng.dingtalk.exception.CustomExceptionEnum;
import com.softeng.dingtalk.entity.AcRecord;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.sdk.Peer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.concurrent.TimeoutException;

@ConditionalOnExpression("${blockchain.service.enable:true}")
@Aspect
@Component
@Slf4j
public class BlockChainAspect {
    @Resource
    private Contract contract;

    @Resource
    private Network network;

    @Resource
    private AcRecordRepository acRecordRepository;

    @Pointcut("execution(* com.softeng.dingtalk.dao.repository.AcRecordRepository.save(..))")
    public void saveRecord(){
    }

    @Pointcut("execution(* com.softeng.dingtalk.dao.repository.AcRecordRepository.delete(..))")
    public void deleteRecord(){
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
        addToBlockChain(param, false);
    }

    @AfterReturning("deleteRecord()")
    public void afterDeleteRecord(JoinPoint point){
        AcRecord param=(AcRecord) point.getArgs()[0];
        addToBlockChain(param, true);
    }

    @AfterReturning("deleteRecordList()")
    public void afterDeleteRecordList(JoinPoint point){
        AcRecord[] param=(AcRecord[]) point.getArgs()[0];
        for (AcRecord acRecord : param) {
            addToBlockChain(acRecord, true);
        }

    }

    @AfterReturning("saveRecordList()")
    public void afterSaveRecordList(JoinPoint point){
        AcRecord[] param=(AcRecord[]) point.getArgs()[0];
        for (AcRecord acRecord : param) {
            addToBlockChain(acRecord, false);
        }
    }

    private void addToBlockChain(AcRecord acRecord, boolean isDeleteType) {
        int userId = acRecord.getUser().getId();
        try {
            if(!userInfoExists(userId)) {
                createUserInfo(userId, acRecord.getUser().getName(), acRecord.getUser().getStuNum(), acRecordRepository.getUserAcSum(userId));
            }
            acChange(userId, (isDeleteType? (-1 * acRecord.getAc()) : acRecord.getAc()) + "",
                    isDeleteType? ("记录删除---" + acRecord.getReason()): acRecord.getReason());
        } catch (ContractException e) {
            CustomExceptionEnum.FABRIC_CONTRACT_EXCEPTION.throwDirectly();
            e.printStackTrace();
        } catch (InterruptedException e) {
            CustomExceptionEnum.FABRIC_INTERRUPT_EXCEPTION.throwDirectly();
            e.printStackTrace();
        } catch (TimeoutException e) {
            CustomExceptionEnum.FABRIC_TIMEOUT_EXCEPTION.throwDirectly();
            e.printStackTrace();
        } catch (Exception e) {
            CustomExceptionEnum.DEFAULT_EXCEPTION.throwDirectly();
            e.printStackTrace();
        }

    }

    private boolean userInfoExists(int userId) throws ContractException {
        byte[] queryAResultBefore = contract.evaluateTransaction("UserInfoExists",userId+"");
//        “true”有四个字节， “false”有五个字节
        return queryAResultBefore.length == 4;
    }

    private void createUserInfo(int userId, String userName, String stuNum, double acValue) throws ContractException, InterruptedException, TimeoutException {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        byte[] invokeResult = contract.createTransaction("CreateUserInfo")
                .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                .submit(userId + "", userName, stuNum, acValue + "", sdf.format(new Date()));

        log.info("智能合约[createUserInfo]--- " + new String(invokeResult, StandardCharsets.UTF_8));
    }

    private void acChange(int userId, String acChange, String reason) throws ContractException, InterruptedException, TimeoutException {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        byte[] invokeResult = contract.createTransaction("AcChange")
                .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                .submit(userId + "", acChange, reason, sdf.format(new Date()));
        log.info("智能合约[acChange]--- " + new String(invokeResult, StandardCharsets.UTF_8));
    }
}
