package com.softeng.dingtalk.fabric;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.softeng.dingtalk.bean.Chaincode;
import com.softeng.dingtalk.bean.Orderers;
import com.softeng.dingtalk.bean.Peers;
import com.softeng.dingtalk.config.FabricConfig;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @Description 智能合约管理器，其中invoke用于插入执行等，query用于查询
 * 以单例的形式生成该对象
 * @Author Jerrian Zhao
 * @Data 03/06/2022
 */
public class ChaincodeManager {
    private static final Logger logger = Logger.getLogger(ChaincodeManager.class);

    private FabricConfig fabricConfig;
    private Orderers orderers;
    private Peers peers;
    private Chaincode chaincode;
    private HFClient client;
    private FabricOrg fabricOrg;
    private Channel channel;
    private ChaincodeID chaincodeID;

    /**
     * 获取组织
     *
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws IOException
     */
    private FabricOrg getFabricOrg() throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        File storeFile = new File(System.getProperty("java.io.tmpdir") + "/HFCSampletest.properties");
        FabricStore fabricStore = new FabricStore(storeFile);
        FabricOrg fabricOrg = new FabricOrg(peers, orderers, fabricStore, fabricConfig.getCryptoConfigPath());
        logger.debug("got FabricOrg");
        return fabricOrg;
    }

    private Channel getChannel(FabricOrg fabricOrg, HFClient client) throws InvalidArgumentException, TransactionException {
        Channel channel = client.newChannel(chaincode.getChannelName());
        logger.debug(chaincode.getChannelName());

        for (int i = 0; i < peers.get().size(); i++) {
            File peerCert = Paths.get(fabricConfig.getCryptoConfigPath(), "/peerOrganizations", peers.getOrgDomainName(), "peers", peers.get().get(i).getPeerName(), "tls/server.crt").toFile();
            if (!peerCert.exists()) {
                throw new RuntimeException(
                        String.format("Missing cert file: %s. No such file found at: %s", peers.get().get(i).getPeerName(), peerCert.getAbsolutePath()));
            }
            Properties peerProperties = new Properties();
            peerProperties.setProperty("pemFile", peerCert.getAbsolutePath());
            // ret.setProperty("trustServerCertificate", "true");
            peerProperties.setProperty("hostnameOverride", peers.getOrgDomainName());
            peerProperties.setProperty("sslProvider", "openSSL");
            peerProperties.setProperty("negotiationType", "TLS");
            // 在grpc的NettyChannelBuilder上设置特定选项
            peerProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);
            channel.addPeer(client.newPeer(peers.get().get(i).getPeerName(), fabricOrg.getPeerLocation(peers.get().get(i).getPeerName()), peerProperties));
            if (peers.get().get(i).isAddEventHub()) {
                channel.addEventHub(
                        client.newEventHub(peers.get().get(i).getPeerEventHubName(), fabricOrg.getEventHubLocation(peers.get().get(i).getPeerEventHubName()), peerProperties));
            }
        }

        for (int i = 0; i < orderers.getOrderers().size(); i++) {
            File ordererCert = Paths.get(fabricConfig.getCryptoConfigPath(), "/ordererOrganizations", orderers.getOrdererDomainName(), "orderers", orderers.getOrderers().get(i).getOrdererName(), "tls/server.crt").toFile();
            if (!ordererCert.exists()) {
                throw new RuntimeException(String.format("Missing cert file for: %s. Could not find at location: %s", orderers.getOrderers().get(i).getOrdererName(), ordererCert.getAbsolutePath()));
            }
            Properties ordererProperties = new Properties();
            ordererProperties.setProperty("pemFile", ordererCert.getAbsolutePath());
            ordererProperties.setProperty("hostnameOverride", orderers.getOrdererDomainName());
            ordererProperties.setProperty("sslProvider", "openSSL");
            ordererProperties.setProperty("negotiationType", "TLS");
            ordererProperties.put("grpc.ManagedChannelBuilderOption.maxInboundMessageSize", 9000000);
            ordererProperties.setProperty("ordererWaitTimeMilliSecs", "300000");
            channel.addOrderer(client.newOrderer(orderers.getOrderers().get(i).getOrdererName(), fabricOrg.getOrdererLocation(orderers.getOrderers().get(i).getOrdererName()), ordererProperties));
        }
        //logger.debug("channel.isInitialized() = " + channel.isInitialized());
        if (!channel.isInitialized()) {
            channel.initialize();
        }
        if (fabricConfig.isRegisterEvent()) {
            channel.registerBlockListener(new BlockListener() {
                @Override
                public void received(BlockEvent event) {
                    logger.debug("========================Event事件监听开始========================");
                    try {
                        logger.debug("event.getChannelId() = " + event.getChannelId());
                        //logger.debug("event.getEvent().getChaincodeEvent().getPayload().toStringUtf8() = " + event.getEvent().getChaincodeEvent().getPayload().toStringUtf8());
                        logger.debug("event.getBlock().getData().getDataList().size() = " + event.getBlock().getData().getDataList().size());
                        ByteString byteString = event.getBlock().getData().getData(0);
                        String result = byteString.toStringUtf8();
                        logger.debug("byteString.toStringUtf8() = " + result);

                        String r1[] = result.split("END CERTIFICATE");
                        String rr = r1[2];
                        logger.debug("rr = " + rr);
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }
                    logger.debug("========================Event事件监听结束========================");
                }
            });
        }
        return channel;
    }

    private Channel getChannel() throws InvalidArgumentException, TransactionException {
        client.setUserContext(fabricOrg.getPeerAdmin());
        return getChannel(fabricOrg, client);
    }

    private ChaincodeID getChaincodeID() {
        return ChaincodeID.newBuilder().setName(chaincode.getChaincodeName()).setVersion(chaincode.getChaincodeVersion()).setPath(chaincode.getChaincodePath()).build();
    }

    public ChaincodeManager(FabricConfig fabricConfig) throws IllegalAccessException, InvocationTargetException, InvalidArgumentException, InstantiationException, NoSuchMethodException, CryptoException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException, IOException, TransactionException {
        this.fabricConfig = fabricConfig;
        orderers = this.fabricConfig.getOrderers();
        peers = this.fabricConfig.getPeers();
        chaincode = this.fabricConfig.getChaincode();
        client = HFClient.createNewInstance();
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

        fabricOrg = getFabricOrg();
        channel = getChannel();
        chaincodeID = getChaincodeID();

        client.setUserContext(fabricOrg.getPeerAdmin());
    }

    /**
     * 执行智能合约
     * @param fcn
     * @param args
     * @return
     * @throws InvalidArgumentException
     * @throws ProposalException
     */
    public Map<String, String> invoke(String fcn, String[] args) throws InvalidArgumentException, ProposalException{
        Map<String, String> resultMap = new HashMap<>();
        Collection<ProposalResponse> successful = new LinkedList<>();
        Collection<ProposalResponse> failed = new LinkedList<>();

        //向所有peers发送transaction proposal
        TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
        transactionProposalRequest.setChaincodeID(chaincodeID);
        transactionProposalRequest.setFcn(fcn);
        transactionProposalRequest.setArgs(args);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
        tm2.put("result", ":)".getBytes(UTF_8));
        transactionProposalRequest.setTransientMap(tm2);

        Collection<ProposalResponse> transactionPropResp = channel.sendTransactionProposal(transactionProposalRequest, channel.getPeers());
        for (ProposalResponse response : transactionPropResp) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                successful.add(response);
            } else {
                failed.add(response);
            }
        }

        Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(transactionPropResp);
        if (proposalConsistencySets.size() != 1) {
            logger.error("Expected only one set of consistent proposal responses but got " + proposalConsistencySets.size());
        }

        if (failed.size() > 0) {
            ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
            logger.error("Not enough endorsers for inspect:" + failed.size() + " endorser error: " + firstTransactionProposalResponse.getMessage() + ". Was verified: "
                    + firstTransactionProposalResponse.isVerified());
            resultMap.put("code", "error");
            resultMap.put("data", firstTransactionProposalResponse.getMessage());
            return resultMap;
        } else {
            logger.info("Successfully received transaction proposal responses.");
            ProposalResponse resp = transactionPropResp.iterator().next();
            byte[] x = resp.getChaincodeActionResponsePayload();
            String resultAsString = null;
            if (x != null) {
                resultAsString = new String(x, UTF_8);
            }
            logger.info("resultAsString = " + resultAsString);
            channel.sendTransaction(successful);
            resultMap.put("code", "success");
            resultMap.put("data", resultAsString);
            return resultMap;
        }

        //        channel.sendTransaction(successful).thenApply(transactionEvent -> {
        //            if (transactionEvent.isValid()) {
        //                log.info("Successfully send transaction proposal to orderer. Transaction ID: " + transactionEvent.getTransactionID());
        //            } else {
        //                log.info("Failed to send transaction proposal to orderer");
        //            }
        //            // chain.shutdown(true);
        //            return transactionEvent.getTransactionID();
        //        }).get(chaincode.getInvokeWatiTime(), TimeUnit.SECONDS);
    }

    /**
     * 查询智能合约
     * @param fcn
     * @param args
     * @return
     * @throws InvalidArgumentException
     * @throws ProposalException
     */
    public Map<String, String> query(String fcn, String[] args) throws InvalidArgumentException, ProposalException{
        Map<String, String> resultMap = new HashMap<>();
        String payload = "";
        QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(args);
        queryByChaincodeRequest.setFcn(fcn);
        queryByChaincodeRequest.setChaincodeID(chaincodeID);

        Map<String, byte[]> tm2 = new HashMap<>();
        tm2.put("HyperLedgerFabric", "QueryByChaincodeRequest:JavaSDK".getBytes(UTF_8));
        tm2.put("method", "QueryByChaincodeRequest".getBytes(UTF_8));
        queryByChaincodeRequest.setTransientMap(tm2);

        Collection<ProposalResponse> queryProposals = channel.queryByChaincode(queryByChaincodeRequest, channel.getPeers());
        for (ProposalResponse proposalResponse : queryProposals) {
            if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
                logger.debug("Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: " + proposalResponse.getStatus() + ". Messages: "
                        + proposalResponse.getMessage() + ". Was verified : " + proposalResponse.isVerified());
                resultMap.put("code", "error");
                resultMap.put("data", "Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: " + proposalResponse.getStatus() + ". Messages: "
                        + proposalResponse.getMessage() + ". Was verified : " + proposalResponse.isVerified());
            } else {
                payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
                logger.debug("Query payload from peer: " + proposalResponse.getPeer().getName());
                logger.debug("" + payload);
                resultMap.put("code", "success");
                resultMap.put("data", payload);
            }
        }
        return resultMap;
    }
}
