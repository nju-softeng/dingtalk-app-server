package com.softeng.dingtalk.dto.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserTeamReq对象", description="用户-研究组请求对象")
public class UserTeamReq implements Serializable {
    private int userId;

    private int teamId;
}
