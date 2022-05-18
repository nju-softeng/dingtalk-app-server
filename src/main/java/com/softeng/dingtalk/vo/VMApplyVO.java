package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.User;
import lombok.Data;
import java.time.LocalDate;

@Data
public class VMApplyVO {

    Integer id;
    User user;
    int state;
    //项目组
    String projectTeam;
    //课题
    String subject;
    //email
    String email;
    //开始时间
    LocalDate start;
    //结束时间
    LocalDate end;
    //申请用途
    String purpose;
    //CPU核心数
    int coreNum;
    //内存大小 GB为单位
    int memory;
    //硬盘容量 GB为单位
    int capacity;
    //操作系统
    String operationSystem;
    //申请日期
    LocalDate applyDate=LocalDate.now();
}
