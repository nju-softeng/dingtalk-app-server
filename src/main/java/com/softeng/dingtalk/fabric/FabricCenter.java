package com.softeng.dingtalk.fabric;

import com.softeng.dingtalk.bean.Chaincode;
import com.softeng.dingtalk.bean.Orderers;
import com.softeng.dingtalk.bean.Peers;
import com.softeng.dingtalk.config.FabricConfig;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

@Slf4j
public class FabricCenter {
    private ChaincodeManager manager;
    private static FabricCenter fabricCenter = null; //单例

    private Peers getPeers(){
        Peers peers = new Peers();
        peers.setOrgName(""); // TODO: 2022/3/6 OrgName
        peers.setOrgMSPID(""); // TODO: 2022/3/6 MspId
        peers.setOrgDomainName(""); // TODO: 2022/3/6 DomainName
        peers.addPeer("","","","",""); // TODO: 2022/3/6 just add a peer!
        return peers;
    }

    private Orderers getOrderers(){
        Orderers orderers = new Orderers();
        orderers.setOrdererDomainName(""); // TODO: 2022/3/6 Orderer域名
        orderers.addOrderer("",""); // TODO: 2022/3/6 一号orderer
        orderers.addOrderer("",""); // TODO: 2022/3/6 二号orderer (add more if u want)
        return orderers;
    }

    private Chaincode getChaincode(String channelName, String chaincodeName, String chaincodePath, String chaincodeVersion) {
        Chaincode chaincode = new Chaincode();
        chaincode.setChannelName(channelName);
        chaincode.setChaincodeName(chaincodeName);
        chaincode.setChaincodePath(chaincodePath);
        chaincode.setChaincodeVersion(chaincodeVersion);
        return chaincode;
    }

    private String getChannelArtifactsPath() {
        String directorys = Objects.requireNonNull(FabricCenter.class.getClassLoader().getResource("fabric")).getFile();
        File directory = new File(directorys);
        return directory.getPath() + "/channel-artifacts/";
    }

    private String getCryptoConfigPath() {
        String directoryStr = Objects.requireNonNull(FabricCenter.class.getClassLoader().getResource("fabric")).getFile();
        File directoryFile = new File(directoryStr);
        return directoryFile.getPath() + "/crypto-config/";
    }

    private FabricConfig getConfig(){
        FabricConfig fabricConfig = new FabricConfig();
        fabricConfig.setPeers(getPeers());
        fabricConfig.setOrderers(getOrderers());
        fabricConfig.setChaincode(getChaincode("xxx", "xxxcc", "github.com/hyperledger/fabric/chaincode/go/release/xxx", "1.0")); // TODO: 2022/3/6 Chaincode
        fabricConfig.setChannelArtifactsPath(getChannelArtifactsPath());
        fabricConfig.setCryptoConfigPath(getCryptoConfigPath());
        return fabricConfig;
    }

    private FabricCenter() throws IOException, TransactionException, InstantiationException, InvocationTargetException, NoSuchMethodException, NoSuchAlgorithmException, IllegalAccessException, InvalidArgumentException, NoSuchProviderException, CryptoException, ClassNotFoundException, InvalidKeySpecException {
        manager = new ChaincodeManager(getConfig());
    }

    public static FabricCenter getFabricCenter() throws IOException, InstantiationException, InvocationTargetException, NoSuchMethodException, NoSuchAlgorithmException, IllegalAccessException, InvalidArgumentException, InvalidKeySpecException, CryptoException, NoSuchProviderException, TransactionException, ClassNotFoundException {
        if (fabricCenter == null){
            synchronized (FabricCenter.class){
                if (fabricCenter == null){
                    fabricCenter = new FabricCenter();
                }
            }
        }
        return fabricCenter;
    }

    public ChaincodeManager getManager(){
        return manager;
    }
}
