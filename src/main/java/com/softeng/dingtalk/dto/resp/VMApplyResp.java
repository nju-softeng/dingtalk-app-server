package com.softeng.dingtalk.dto.resp;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="VMApplyResp对象", description="虚拟机申请响应对象")
public class VMApplyResp implements Serializable {
    private Integer id;

    private UserResp user;
    //状态 -1审核不通过，0审核中，1审核通过
    private Integer state;
    //项目组
    private String projectTeam;
    //课题
    private String subject;
    //email
    private String email;
    //开始时间
    private LocalDate start;
    //结束时间
    private LocalDate end;
    //申请用途
    private String purpose;
    //CPU核心数
    private Integer coreNum;
    //内存大小 GB为单位
    private Integer memory;
    //硬盘容量 GB为单位
    private Integer capacity;
    //操作系统
    private String operationSystem;
    //申请日期
    private LocalDate applyDate;
}
