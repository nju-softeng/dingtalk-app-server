package com.softeng.dingtalk.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description
 * @create 2/5/2020 5:14 PM
 */
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private Paper paper;
    private LocalDateTime createTime;
    private LocalDateTime expiryTime;

}
