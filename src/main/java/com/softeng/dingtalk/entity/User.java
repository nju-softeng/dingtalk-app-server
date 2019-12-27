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
    @Column(unique = true)
    private String userid;    //钉钉用户userID
    private String name;      //用户姓名
    private String avatar;    //用户头像
    private int successCnt;   //连续成功次数
    private String degree;    //学位
    private int authority = USER_AUTHORITY;  //用户权限
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;  //插入时间

    @JsonIgnore
    @OneToMany(mappedBy = "applicant", cascade = CascadeType.REMOVE)
    private List<Application> applicationlist;  //用户的所有绩效申请
    @JsonIgnore
    @OneToMany(mappedBy = "auditor", cascade = CascadeType.REMOVE)
    private List<Application> checklist;        //审核人收到的审核
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<DcRecord> dcRecords;           //用户的所有DC绩效记录
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<AcRecord> acRecords;           //用户的所有AC日志

    public User(String userid, String name, String avatar, int authority) {
        this.userid = userid;
        this.name = name;
        this.avatar = avatar;
        this.authority = authority;
    }
}
