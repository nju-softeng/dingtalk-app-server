package com.softeng.dingtalk.entity.cpk;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author LiXiaoKang
 * @description 用户-权限表的联合主键(CompositePrimaryKey)
 * @date 02/02/2023
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserPermissionCPK implements Serializable {

    private int userId;

    private int permissionId;

    @Override
    public boolean equals(Object obj){
        if(obj instanceof UserPermissionCPK){
            return userId == ((UserPermissionCPK) obj).userId && permissionId == ((UserPermissionCPK) obj).getPermissionId();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return userId * 10000 + permissionId;
    }
}
