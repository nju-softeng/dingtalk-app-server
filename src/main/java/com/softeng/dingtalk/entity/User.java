package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description 用户实体类
 * @date 11/13/2019
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private int userid;       //钉钉用户userID
    private String name;      //用户姓名
    private String degree;    //学位
    private Double currentAC; //当前AC值
    private int continuousAchievementsCnt;   //连续成功次数

    //定义静态常量表示用户权限
    public static final int USER_AUTHORITY = 0;
    public static final int AUDITOR_AUTHORITY = 1;
    public static final int ADMIN_AUTHORITY = 1;

    private int authority = USER_AUTHORITY;  //用户权限

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;

}
