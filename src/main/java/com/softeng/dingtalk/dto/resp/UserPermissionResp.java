package com.softeng.dingtalk.dto.resp;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserPermissionResp对象", description="用户-权限响应对象")
public class UserPermissionResp implements Serializable {
    private int userId;

    private int permissionId;
}
