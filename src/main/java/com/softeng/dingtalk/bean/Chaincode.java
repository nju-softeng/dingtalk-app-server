package com.softeng.dingtalk.bean;

/**
 * @Description Fabric chaincode信息，包括channel等
 * @Author Jerrian Zhao
 * @Data 03/03/2022
 */
public class Chaincode {
    private String channelName; //当前智能合约所述频道名
    private String chaincodeName; //智能合约名
    private String chaincodePath; //智能合约安装路径
    private String chaincodeVersion; //版本号
    private int invokeWaitTime = 100000; //执行操作等待时间
    private int deployWaitTime = 120000; //执行实例等待时间

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChaincodeName() {
        return chaincodeName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getChaincodePath() {
        return chaincodePath;
    }

    public void setChaincodePath(String chaincodePath) {
        this.chaincodePath = chaincodePath;
    }

    public String getChaincodeVersion() {
        return chaincodeVersion;
    }

    public void setChaincodeVersion(String chaincodeVersion) {
        this.chaincodeVersion = chaincodeVersion;
    }

    public int getInvokeWatiTime() {
        return invokeWaitTime;
    }

    public void setInvokeWatiTime(int invokeWatiTime) {
        this.invokeWaitTime = invokeWatiTime;
    }

    public int getDeployWaitTime() {
        return deployWaitTime;
    }

    public void setDeployWaitTime(int deployWaitTime) {
        this.deployWaitTime = deployWaitTime;
    }
}
