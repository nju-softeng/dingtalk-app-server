package com.softeng.dingtalk.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * @author LiXiaoKang
 * @description 用户组实体类
 * @date 02/02/2023
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
// 设置联合主键的注解
@IdClass(UserTeamAssociatePK.class)
public class UserTeam {

    @Id
    private int userId;

    @Id
    private int teamId;
}
