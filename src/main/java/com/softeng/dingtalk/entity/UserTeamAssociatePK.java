package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author LiXiaoKang
 * @description 用户-用户组实体类的联合主键
 * @date 02/02/2023
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserTeamAssociatePK implements Serializable {

    private int userId;

    private int teamId;

    @Override
    public boolean equals(Object obj){
        if(obj instanceof UserTeamAssociatePK){
            return userId == ((UserTeamAssociatePK) obj).userId && teamId == ((UserTeamAssociatePK) obj).getTeamId();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return userId+teamId;
    }
}
