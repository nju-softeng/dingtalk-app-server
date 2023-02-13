package com.softeng.dingtalk.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author LiXiaoKang
 * @since 2023-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserPermission实体对象", description="用户与其用户权限的管理")
public class UserPermission {
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "权限id")
    private Integer permissionId;
}
