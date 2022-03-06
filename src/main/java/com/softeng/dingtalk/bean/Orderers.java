package com.softeng.dingtalk.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description orderer信息，采取集群方案（但是可以只放一个）
 * @Author Jerrian Zhao
 * @Data 03/03/2022
 */
public class Orderers {
    private String ordererDomainName; //orderer服务器所在根域名
    private List<SingleOrderer> orderers; //orderer服务器集合

    public Orderers() {
        orderers = new ArrayList<>();
    }

    public String getOrdererDomainName() {
        return ordererDomainName;
    }

    public void setOrdererDomainName(String ordererDomainName) {
        this.ordererDomainName = ordererDomainName;
    }

    public void addOrderer(String name, String location) {
        orderers.add(new SingleOrderer(name, location));
    }

    public List<SingleOrderer> getOrderers() {
        return orderers;
    }

    /**
     * orderer对象
     */

    public class SingleOrderer {
        private String ordererName; //orderer域名
        private String ordererLocation; //orderer访问地址

        public SingleOrderer(String ordererName, String ordererLocation) {
            super();
            this.ordererName = ordererName;
            this.ordererLocation = ordererLocation;
        }

        public String getOrdererName() {
            return ordererName;
        }

        public void setOrdererName(String ordererName) {
            this.ordererName = ordererName;
        }

        public String getOrdererLocation() {
            return ordererLocation;
        }

        public void setOrdererLocation(String ordererLocation) {
            this.ordererLocation = ordererLocation;
        }
    }
}
