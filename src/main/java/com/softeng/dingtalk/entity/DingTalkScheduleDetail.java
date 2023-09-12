package com.softeng.dingtalk.entity;

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
public class DingTalkScheduleDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToOne
    @JoinColumn(name = "ac_record_id")
    AcRecord acRecord;

    @ManyToOne
    @JsonIgnore
    DingTalkSchedule dingTalkSchedule;

    public DingTalkScheduleDetail(User user, DingTalkSchedule dingTalkSchedule) {
        this.user = user;
        this.dingTalkSchedule = dingTalkSchedule;
    }
}
