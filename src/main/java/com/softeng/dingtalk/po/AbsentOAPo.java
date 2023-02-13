package com.softeng.dingtalk.po;

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
@Table(name = "absentoa")
public class AbsentOAPo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    DingTalkSchedulePo dingTalkSchedule;

    @OneToOne
    @JoinColumn(name = "user_id")
    UserPo user;
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
    // -1待定,0拒绝,1通过
    int state=-1;
    //是否通过
    boolean isPass=false;
    //请求Id
    String processInstanceId;

    public AbsentOAPo(String type, LocalDate start, LocalDate end, Double dayNum, String reason) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.dayNum = dayNum;
        this.reason = reason;
    }
}
