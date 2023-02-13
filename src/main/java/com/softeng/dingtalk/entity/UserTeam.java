package com.softeng.dingtalk.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 * @author lilingj
 * @since 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserTeam实体对象", description="用户与研究组关系")
public class UserTeam implements Serializable {


    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "组id")
    private Integer teamId;
}
