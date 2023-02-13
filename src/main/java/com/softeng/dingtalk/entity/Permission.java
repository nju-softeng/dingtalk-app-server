package com.softeng.dingtalk.entity;

import com.softeng.dingtalk.enums.PermissionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 * @author LiXiaoKang
 * @since 2023-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="Permission实体对象", description="权限")
public class Permission implements Serializable {

    @ApiModelProperty(value = "权限id")
    private Integer id;

    @ApiModelProperty(value = "权限名")
    private String name;

    @ApiModelProperty(value = "权限描述")
    private String description;

    public Permission(PermissionEnum permissionEnum) {
        this.id = permissionEnum.getCode();
        this.name = permissionEnum.getName();
        this.description = permissionEnum.getDescription();
    }

}
