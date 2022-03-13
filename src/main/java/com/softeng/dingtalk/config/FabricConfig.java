package com.softeng.dingtalk.config;

import com.softeng.dingtalk.bean.Chaincode;
import com.softeng.dingtalk.bean.Orderers;
import com.softeng.dingtalk.bean.Peers;
import com.softeng.dingtalk.fabric.ChaincodeManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Objects;

/**
 * @Description fabric 配置
 * @Author Jerrian Zhao
 * @Data 03/06/2022
 */
public class FabricConfig {
    private static final Logger logger = Logger.getLogger(FabricConfig.class);

    private Peers peers;
    private Orderers orderers;
    private Chaincode chaincode;
    private String channelArtifactsPath;
    private String cryptoConfigPath;
    private boolean registerEvent = false;

    private String getChannelPath() {
        String directorys = Objects.requireNonNull(ChaincodeManager.class.getClassLoader().getResource("fabric")).getFile();
        logger.debug("directorys = " + directorys);
        File directory = new File(directorys);
        logger.debug("directory = " + directory.getPath());

        return directory.getPath();
    }

    public FabricConfig() {
        channelArtifactsPath = getChannelPath() + "/channel-artifacts/";
        cryptoConfigPath = getChannelPath() + "/crypto-config/";
    }

    public Peers getPeers() {
        return peers;
    }

    public void setPeers(Peers peers) {
        this.peers = peers;
    }

    public Orderers getOrderers() {
        return orderers;
    }

    public void setOrderers(Orderers orderers) {
        this.orderers = orderers;
    }

    public Chaincode getChaincode() {
        return chaincode;
    }

    public void setChaincode(Chaincode chaincode) {
        this.chaincode = chaincode;
    }

    public String getChannelArtifactsPath() {
        return channelArtifactsPath;
    }

    public void setChannelArtifactsPath(String channelArtifactsPath) {
        this.channelArtifactsPath = channelArtifactsPath;
    }

    public String getCryptoConfigPath() {
        return cryptoConfigPath;
    }

    public void setCryptoConfigPath(String cryptoConfigPath) {
        this.cryptoConfigPath = cryptoConfigPath;
    }

    public boolean isRegisterEvent() {
        return registerEvent;
    }

    public void setRegisterEvent(boolean registerEvent) {
        this.registerEvent = registerEvent;
    }
}
