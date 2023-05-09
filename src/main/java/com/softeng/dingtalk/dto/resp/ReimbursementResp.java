package com.softeng.dingtalk.dto.resp;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ReimbursementResp对象", description="报销申请响应对象")
public class ReimbursementResp implements Serializable {

    private Integer id;

    private UserResp user;

    //审核状态： -1 未审核， 0 审核中，1 审核通过， 2 审核不通过
    private int state;
    private String name;
    // 差旅报销，国内会议报销，国际会议报销，办公用品报销
    private String type;

    private String path;
}
