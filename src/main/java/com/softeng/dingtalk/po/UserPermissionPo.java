package com.softeng.dingtalk.po;

import com.softeng.dingtalk.po.cpk.UserPermissionCPK;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author LiXiaoKang
 * @description 用户-用户权限实体类
 * @date 02/02/2023
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
// 设置联合主键的注解
@IdClass(UserPermissionCPK.class)
@Table(name = "user_permission")
public class UserPermissionPo {

    /**
     * 对应用户表的id字段（int），而非钉钉的userid（string）
     */
    @Id
    private int userId;

    /**
     * 权限id
     */
    @Id
    private int permissionId;
}
