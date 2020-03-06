package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    //定义静态常量表示用户权限
    public static final int USER_AUTHORITY = 0;
    public static final int AUDITOR_AUTHORITY = 1;
    public static final int ADMIN_AUTHORITY = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonIgnore
    @Column(unique = true)
    private String userid;    //钉钉用户userID
    private String name;      //用户姓名
    private String avatar;    //用户头像
    private int authority = USER_AUTHORITY;  //用户权限
    private String position;
    @JsonIgnore
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;  //插入时间
    private int degree; //

    public User(String userid, String name, String avatar, int authority, String position) {
        this.userid = userid;
        this.name = name;
        this.avatar = avatar;
        this.authority = authority;
        this.position = position;
    }

    public User(int id) {
        this.id = id;
    }
}
