package com.softeng.dingtalk.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author LiXiaoKang
 * @since 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Team实体对象", description="用户研究组")
public class Team implements Serializable {

    @ApiModelProperty(value = "组id")
    private Integer id;

    @ApiModelProperty(value = "用户研究组名")
    private String name;

}
