package com.softeng.dingtalk.po;


import com.softeng.dingtalk.po.cpk.UserTeamCPK;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @author LiXiaoKang
 * @description 用户组表类
 * @date 02/02/2023
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
// 设置联合主键的注解
@IdClass(UserTeamCPK.class)
@Table(name = "user_team")
public class UserTeamPo {

    /**
     * 对应用户表的id字段（int），而非钉钉的userid（string）
     */
    @Id
    private int userId;

    /**
     * 用户组id
     */
    @Id
    private int teamId;
}
