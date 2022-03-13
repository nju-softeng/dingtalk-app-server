package com.softeng.dingtalk.fabric;

import com.softeng.dingtalk.bean.Orderers;
import com.softeng.dingtalk.bean.Peers;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 * @Description
 * @Author Jerrian Zhao
 * @Data 03/04/2022
 */
public class FabricOrg {
    private static Logger logger = Logger.getLogger(FabricOrg.class);

    private String name; //组织名
    private String mspId; //会员Id
    private HFCAClient caClient; //CA客户端

    Map<String, User> userMap = new HashMap<>(); //用户
    Map<String, String> peerLocations = new HashMap<>(); //节点地址
    Map<String, String> ordererLocations = new HashMap<>(); //orderer地址
    Map<String, String> eventHubLocations = new HashMap<>(); //事件地址
    Set<Peer> peers = new HashSet<>(); //节点
    private FabricUser admin; //联盟管理员
    private String CALocation; //CA地址
    private Properties caProperties = null;
    private FabricUser peerAdmin; //节点管理员
    private String domainName; //域名

    public FabricOrg(Peers peers, Orderers orderers, FabricStore fabricStore, String cryptoConfigPath) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException {
        this.name = peers.getOrgName();
        this.mspId = peers.getOrgMSPID();
        for (int i = 0; i < peers.get().size(); i++) {
            addPeerLocation(peers.get().get(i).getPeerName(), peers.get().get(i).getPeerLocation());
            addEventHubLocation(peers.get().get(i).getPeerEventHubName(), peers.get().get(i).getPeerEventHubLocation());
            setCALocation(peers.get().get(i).getCaLocation());
        }
        for (int i = 0; i < orderers.getOrderers().size(); i++) {
            addOrdererLocation(orderers.getOrderers().get(i).getOrdererName(), orderers.getOrderers().get(i).getOrdererLocation());
        }
        setDomainName(peers.getOrgDomainName());

        // Set up HFCA
        // setCAClient(HFCAClient.createNewInstance(peers.getCaLocation(), getCAProperties()));

        setAdmin(fabricStore.getMember("admin", peers.getOrgName())); // 设置组织管理员
        File skFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", peers.getOrgDomainName(), String.format("/users/Admin@%s/msp/keystore", peers.getOrgDomainName())).toFile();
        File certificateFile = Paths.get(cryptoConfigPath, "/peerOrganizations/", peers.getOrgDomainName(),
                String.format("/users/Admin@%s/msp/signcerts/Admin@%s-cert.pem", peers.getOrgDomainName(), peers.getOrgDomainName())).toFile();
        logger.debug("skFile = " + skFile.getAbsolutePath());
        logger.debug("certificateFile = " + certificateFile.getAbsolutePath());
        setPeerAdmin(fabricStore.getMember(peers.getOrgName() + "Admin", peers.getOrgName(), peers.getOrgMSPID(), findSk(skFile), certificateFile)); //节点管理员可以创建通道，连接对等点，并安装链码
    }

    private File findSk(File directory) {
        File[] sks = directory.listFiles((dir, name) -> name.endsWith("_sk"));
        if (sks == null) {
            throw new RuntimeException(String.format("Nothing found. Please make sure %s exist.", directory.getAbsoluteFile().getName()));
        }
        if (sks.length != 1) {
            throw new RuntimeException(String.format("Expected %s only 1 sk but found %d", directory.getAbsoluteFile().getName(), sks.length));
        }
        return sks[0];
    }

    public String getName() {
        return name;
    }

    public String getMspId() {
        return mspId;
    }

    public HFCAClient getCaClient() {
        return caClient;
    }

    public void setCaClient(HFCAClient caClient) {
        this.caClient = caClient;
    }

    /**
     * 根据用户名获得用户
     * @param name
     * @return
     */
    public User getUser(String name) {
        return userMap.get(name);
    }

    public void addUser(FabricUser user) {
        userMap.put(user.getName(),user);
    }

    public String getPeerLocation(String name) {
        return peerLocations.get(name);
    }

    /**
     * 添加节点地址
     * @param name
     * @param location
     */
    public void addPeerLocation(String name, String location) {
        peerLocations.put(name, location);
    }

    public String getOrdererLocation(String name) {
        return ordererLocations.get(name);
    }

    /**
     * 添加orderer地址
     * @param name
     * @param location
     */
    public void addOrdererLocation(String name, String location) {
        ordererLocations.put(name, location);
    }

    /**
     * 获取不可修改的节点key集合
     * @return
     */
    public Set<String> getOrdererNames() {
        return Collections.unmodifiableSet(ordererLocations.keySet());
    }

    /**
     * 获取不可修改的节点地址集合
     * @return
     */
    public Collection<String> getOrdererLocations() {
        return Collections.unmodifiableCollection(ordererLocations.values());
    }

    public String getEventHubLocation(String name) {
        return eventHubLocations.get(name);
    }

    /**
     * 添加事件地址
     * @param name
     * @param location
     */
    public void addEventHubLocation(String name, String location) {
        eventHubLocations.put(name, location);
    }

    /**
     * 获取不可修改的事件key集合
     * @return
     */
    public Set<String> getEventHubNames() {
        return Collections.unmodifiableSet(eventHubLocations.keySet());
    }

    /**
     * 获取不可修改的事件地址集合
     * @return
     */
    public Collection<String> getEventHubLocations() {
        return Collections.unmodifiableCollection(eventHubLocations.values());
    }

    /**
     * 获取不可修改的节点key集合
     * @return
     */
    public Set<String> getPeerNames() {
        return Collections.unmodifiableSet(peerLocations.keySet());
    }

    /**
     * 获取不可修改的节点集合
     * @return
     */
    public Set<Peer> getPeers() {
        return Collections.unmodifiableSet(peers);
    }

    public void addPeer(Peer peer) {
        peers.add(peer);
    }

    public FabricUser getAdmin() {
        return admin;
    }

    public void setAdmin(FabricUser admin) {
        this.admin = admin;
    }

    public String getCALocation() {
        return CALocation;
    }

    public void setCALocation(String CALocation) {
        this.CALocation = CALocation;
    }

    public Properties getCaProperties() {
        return caProperties;
    }

    public void setCaProperties(Properties caProperties) {
        this.caProperties = caProperties;
    }

    public FabricUser getPeerAdmin() {
        return peerAdmin;
    }

    public void setPeerAdmin(FabricUser peerAdmin) {
        this.peerAdmin = peerAdmin;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

}
