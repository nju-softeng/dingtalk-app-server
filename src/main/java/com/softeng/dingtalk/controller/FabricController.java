package com.softeng.dingtalk.controller;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.sdk.Peer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.concurrent.TimeoutException;

@Slf4j
@ConditionalOnExpression("${blockchain.service.enable:true}")
@RestController
@RequestMapping("/api/v2")
public class FabricController {
    @Resource
    private Contract contract;

    @Resource
    private Network network;

    @GetMapping("/UserInfoExists")
    public String UserInfoExists(String userId) throws ContractException {
        byte[] queryAResultBefore = contract.evaluateTransaction("UserInfoExists",userId);
        return new String(queryAResultBefore, StandardCharsets.UTF_8);
    }

    @GetMapping("/createUserInfo")
    public String createUserInfo(String userId, String userName, String stuNum, String acValue) throws ContractException, InterruptedException, TimeoutException {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        byte[] invokeResult = contract.createTransaction("CreateUserInfo")
                .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                .submit(userId, userName, stuNum, acValue, sdf.format(new Date()));
        return new String(invokeResult, StandardCharsets.UTF_8);
    }

    @GetMapping("/AcChange")
    public String acChange(String userId, String acChange, String reason) throws ContractException, InterruptedException, TimeoutException {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        byte[] invokeResult = contract.createTransaction("AcChange")
                .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                .submit(userId, acChange, reason, sdf.format(new Date()));
        return new String(invokeResult, StandardCharsets.UTF_8);
    }

    @GetMapping("/queryAll")
    public String queryAll() throws ContractException {
        byte[] queryAResultBefore = contract.evaluateTransaction("GetAllAssets");
        return new String(queryAResultBefore, StandardCharsets.UTF_8);
    }

    @GetMapping("/getHistory")
    public String getHistory(String userId) throws ContractException {
        byte[] queryAResultBefore = contract.evaluateTransaction("GetHistory", userId);
        return new String(queryAResultBefore, StandardCharsets.UTF_8);
    }
}
