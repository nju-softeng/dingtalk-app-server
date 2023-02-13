package com.softeng.dingtalk.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author LiXiaoKang
 * @since 2023-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PermissionResp对象", description="权限响应对象")
public class PermissionResp implements Serializable {
    @ApiModelProperty(value = "权限id")
    private Integer id;

    @ApiModelProperty(value = "权限名")
    private String name;

    @ApiModelProperty(value = "权限描述")
    private String description;
}
