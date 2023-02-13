package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.po.UserPo;
import lombok.Data;
import java.time.LocalDate;
@Data
public class PracticeVO {

    private Integer id;

    UserPo userPo;
    //实习单位
    String companyName;
    //实习部门
    String department;
    //开始时间
    LocalDate start;
    //结束时间
    LocalDate end;
    //状态 -1拒绝，0审核中，1通过
    int state=0;
}
