package com.softeng.dingtalk.dto.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ReimbursementReq对象", description="报销申请请求对象")
public class ReimbursementReq implements Serializable {
    private int state;
    private String name;
    private String type;
    private String path;
    private int userId;
}
