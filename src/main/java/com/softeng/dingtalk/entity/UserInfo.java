package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author zhanyeye
 * @description
 * @create 2/25/2020 12:29 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int uid;
    private int deliveryCnt; // 连续成功交付次数
    private double voucher;  // 缺陷代金券

}
