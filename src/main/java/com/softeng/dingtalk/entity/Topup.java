package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description
 * @create 2/27/2020 5:35 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Topup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String reason;
    private double subsidy;
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private User user;

}
