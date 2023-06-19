package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softeng.dingtalk.enums.Position;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDate;
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
@ToString
@Accessors(chain = true)
@Table(name = "user")
@SQLDelete(sql = "update `user` set is_deleted = 1 where id = ?")
public class User {
    /**
     * 表示用户权限的静态常量
     */
    public static final int NORMAL_AUTHORITY = 0;
    public static final int AUDITOR_AUTHORITY = 1;
    public static final int ADMIN_AUTHORITY = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 钉钉用户userID
     */
    @Column(unique = true)
    private String userid;

    /**
     * 钉钉文档解释：员工在当前开发者企业账号范围内的唯一标识，系统生成，固定值，不会改变
     */
    private String unionid;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 学号
     */
    private String stuNum;

    /**
     * 用户权限
     */
    @Deprecated
    private int authority = NORMAL_AUTHORITY;

    /**
     * 用户职（学）位
     */
    private Position position = Position.OTHER;

    /**
     * 插入时间
     */
    @JsonIgnore
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;

    /**
     * 软删除标识
     */
    @Column(nullable = false, name = "is_deleted")
    private boolean deleted;



    /**
     * @Author Jerrian Zhao
     * @Data 01/22/2022
     */


    /**
     * 本科学校
     */

    private String undergraduateCollege;

    /**
     * 硕士学校
     */

    private String masterCollege;

    /**
     * 身份证号
     */
    private String idCardNo;

    /**
     * 银行卡号
     */
    private String creditCard;

    /**
     * 开户行
     */
    private String bankName;

    private String leaseContractFileName;
    private String leaseContractFilePath;
    /**
     * 租房开始时间
     */
    @Deprecated
    private LocalDate rentingStart;

    /**
     * 租房结束时间
     */
    @Deprecated
    private LocalDate rentingEnd;

    /**
     * 住址
     */
    private String address;

    /**
     * 状态
     * true为在实习，false为在校
     */
    private Boolean workState;

    /**
     * 备注
     */
    private String remark;

    /**
     * 电话号码
     */
    private String tel;


    /**
     * 获奖情况
     */
    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Prize> allPrizes;


    public User(String userid, String unionid, String name, String avatar, int authority, Position position) {
        this.userid = userid;
        this.unionid = unionid;
        this.name = name;
        this.avatar = avatar;
        this.authority = authority;
        this.position = position;
    }

    public User(int id) {
        this.id = id;
    }
}
