package com.softeng.dingtalk.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author zhanyeye
 * @description 论文
 * @create 2/5/2020 4:51 PM
 */
public class PaperDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Paper paper;
    private User user;

}
