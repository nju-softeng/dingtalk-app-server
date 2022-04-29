package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class AbsentOA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    DingTalkSchedule dingTalkSchedule;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
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
    boolean isPass;
    //请求Id
    String processInstanceId;

}
