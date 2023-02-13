package com.softeng.dingtalk.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "ding_talk_schedule_detail")
public class DingTalkScheduleDetailPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    UserPo user;

    @OneToOne
    @JoinColumn(name = "ac_record_id")
    AcRecordPo acRecord;

    @ManyToOne
    @JsonIgnore
    DingTalkSchedulePo dingTalkSchedule;

    public DingTalkScheduleDetailPo(UserPo user, DingTalkSchedulePo dingTalkSchedule) {
        this.user = user;
        this.dingTalkSchedule = dingTalkSchedule;
    }
}
