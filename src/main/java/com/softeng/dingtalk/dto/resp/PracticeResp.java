package com.softeng.dingtalk.dto.resp;

import com.softeng.dingtalk.po_entity.User;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PracticeResp对象", description="实习申请响应对象")
public class PracticeResp implements Serializable {
    private Integer id;

    private UserResp user;
    //实习单位
    private String companyName;
    //实习部门
    private String department;
    //开始时间
    private LocalDate start;
    //结束时间
    private LocalDate end;
    //状态 -1拒绝，0审核中，1通过
    private int state;
}
