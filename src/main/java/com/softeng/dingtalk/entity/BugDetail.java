package com.softeng.dingtalk.entity;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description
 * @create 3/20/2020 8:49 AM
 */
public class BugDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private Bug bug;

    private User user;
    private boolean principal; // 是否为主要责任人
    private double ac;
    private AcRecord acRecord;
}
