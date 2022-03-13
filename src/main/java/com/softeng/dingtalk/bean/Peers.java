package com.softeng.dingtalk.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description peer信息，包含cli、org、ca、couchDB等关联启动服务信息
 * @Author Jerrian Zhao
 * @Data 03/03/2022
 */
public class Peers {
    private String orgName; //组织名称
    private String orgMSPID; //组织会员ID
    private String orgDomainName; //组织所在根域名
    private List<Peer> peers;

    public Peers() {
        peers = new ArrayList<>();
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgMSPID() {
        return orgMSPID;
    }

    public void setOrgMSPID(String orgMSPID) {
        this.orgMSPID = orgMSPID;
    }

    public String getOrgDomainName() {
        return orgDomainName;
    }

    public void setOrgDomainName(String orgDomainName) {
        this.orgDomainName = orgDomainName;
    }

    public void addPeer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, String caLocation) {
        peers.add(new Peer(peerName, peerEventHubName, peerLocation, peerEventHubLocation, caLocation));
    }

    public List<Peer> get() {
        return peers;
    }

    /**
     * 节点服务器对象
     */
    public class Peer {
        private String peerName; //组织节点域名
        private String peerEventHubName; //节点事件域名
        private String peerLocation; //节点访问地址
        private String peerEventHubLocation; //节点事件监听访问地址
        private String caLocation; //节点ca访问地址
        private boolean addEventHub = false; //该peer是否增加Event事件处理

        public Peer(String peerName, String peerEventHubName, String peerLocation, String peerEventHubLocation, String caLocation) {
            this.peerName = peerName;
            this.peerEventHubName = peerEventHubName;
            this.peerLocation = peerLocation;
            this.peerEventHubLocation = peerEventHubLocation;
            this.caLocation = caLocation;
        }

        public String getPeerName() {
            return peerName;
        }

        public void setPeerName(String peerName) {
            this.peerName = peerName;
        }

        public String getPeerEventHubName() {
            return peerEventHubName;
        }

        public void setPeerEventHubName(String peerEventHubName) {
            this.peerEventHubName = peerEventHubName;
        }

        public String getPeerLocation() {
            return peerLocation;
        }

        public void setPeerLocation(String peerLocation) {
            this.peerLocation = peerLocation;
        }

        public String getPeerEventHubLocation() {
            return peerEventHubLocation;
        }

        public void setPeerEventHubLocation(String peerEventHubLocation) {
            this.peerEventHubLocation = peerEventHubLocation;
        }

        public String getCaLocation() {
            return caLocation;
        }

        public void setCaLocation(String caLocation) {
            this.caLocation = caLocation;
        }

        public boolean isAddEventHub() {
            return addEventHub;
        }

        public void setAddEventHub(boolean addEventHub) {
            this.addEventHub = addEventHub;
        }
    }
}
