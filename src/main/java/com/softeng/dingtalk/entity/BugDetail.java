package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description
 * @create 3/20/2020 8:49 AM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class BugDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private Bug bug;
    @ManyToOne
    private User user;
    private boolean principal; // 是否为主要责任人
    private double ac;
    @ManyToOne(fetch = FetchType.LAZY)
    private AcRecord acRecord;
}
