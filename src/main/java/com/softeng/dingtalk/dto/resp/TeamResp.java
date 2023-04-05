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
@ApiModel(value="TeamReq对象", description="用户研究组响应对象")
public class TeamResp implements Serializable {
    @ApiModelProperty(value = "组id")
    private int id;

    @ApiModelProperty(value = "组名")
    private String name;
}
