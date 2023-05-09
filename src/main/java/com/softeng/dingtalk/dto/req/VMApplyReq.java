package com.softeng.dingtalk.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="VMApplyReq对象", description="虚拟机申请请求对象")
public class VMApplyReq implements Serializable {

    @ApiModelProperty(value = "申请项目组")
    private String projectTeam;

    @ApiModelProperty(value = "申请当前状态")
    private int state;

    @ApiModelProperty(value = "申请人id")
    private int userId;
}
