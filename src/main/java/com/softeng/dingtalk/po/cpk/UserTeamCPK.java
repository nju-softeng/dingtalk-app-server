package com.softeng.dingtalk.po.cpk;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author LiXiaoKang
 * @description 用户-用户组表的联合主键(CompositePrimaryKey)
 * @date 02/02/2023
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserTeamCPK implements Serializable {

    private int userId;

    private int teamId;

    @Override
    public boolean equals(Object obj){
        if(obj instanceof UserTeamCPK){
            return userId == ((UserTeamCPK) obj).userId && teamId == ((UserTeamCPK) obj).getTeamId();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return userId+teamId;
    }
}
