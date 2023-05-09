package com.softeng.dingtalk.dto.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PracticeReq对象", description="实习申请请求对象")
public class PracticeReq {
    private Integer id;
    //实习单位
    private String companyName;
    //实习部门
    private String department;
    //开始时间
    private LocalDate start;
    //结束时间
    private LocalDate end;
    //状态 -1拒绝，0审核中，1通过
    private int state=0;

    private int userId;
}
