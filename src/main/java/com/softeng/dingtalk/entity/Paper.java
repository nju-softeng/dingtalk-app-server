package com.softeng.dingtalk.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * @author zhanyeye
 * @description 论文实体类
 * @create 2/5/2020 4:33 PM
 */
public class Paper {
    //定义静态常量表示用户权限
    public static final int WAIT = 0;
    public static final int ACCEPT = 1;
    public static final int REJECT= 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String journal;
    private LocalDate date;
    private int grade;
    private int result;
}
