package com.softeng.dingtalk.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "vmapply")
public class VMApplyPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    UserPo user;
    //状态 -1审核不通过，0审核中，1审核通过
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
    LocalDate applyDate;

    public VMApplyPo(UserPo user, String projectTeam, String subject, String email,
                     LocalDate start, LocalDate end, String purpose, int coreNum,
                     int memory, int capacity, String operationSystem, LocalDate applyDate) {
        this.user = user;
        this.projectTeam = projectTeam;
        this.subject = subject;
        this.email = email;
        this.start = start;
        this.end = end;
        this.purpose = purpose;
        this.coreNum = coreNum;
        this.memory = memory;
        this.capacity = capacity;
        this.operationSystem = operationSystem;
        this.applyDate = applyDate;
    }

    public void update(String projectTeam, String subject, String email,
                          LocalDate start, LocalDate end, String purpose, int coreNum,
                          int memory, int capacity, String operationSystem) {
        this.projectTeam = projectTeam;
        this.subject = subject;
        this.email = email;
        this.start = start;
        this.end = end;
        this.purpose = purpose;
        this.coreNum = coreNum;
        this.memory = memory;
        this.capacity = capacity;
        this.operationSystem = operationSystem;
        this.applyDate = applyDate;
    }
}
