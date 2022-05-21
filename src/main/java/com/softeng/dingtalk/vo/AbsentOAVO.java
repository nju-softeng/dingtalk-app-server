package com.softeng.dingtalk.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AbsentOAVO {
    Integer id;
    //请假类型 事假 病假 其它
    String type;
    //开始时间
    LocalDate start;
    //结束时间
    LocalDate end;
    //请假天数
    Double dayNum;
    //请假缘由
    String reason;
    //是否通过
    boolean isPass=false;
    //请求Id
    String processInstanceId;

    public AbsentOAVO(String type, String reason) {
        this.type = type;
        this.reason = reason;
    }
}
