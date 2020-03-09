package com.softeng.dingtalk.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author zhanyeye
 * @description
 * @create 3/8/2020 11:26 PM
 */
public class Bug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

}
