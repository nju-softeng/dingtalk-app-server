package com.softeng.dingtalk.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "ding_talk_schedule")
public class DingTalkSchedulePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserPo organizer;

    @OneToMany(mappedBy = "dingTalkSchedule",cascade = {CascadeType.REMOVE})
    List<AbsentOAPo> absentOAList;

    @OneToMany(mappedBy = "dingTalkSchedule",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<DingTalkScheduleDetailPo> dingTalkScheduleDetailList;
    //标题
    String summary;
    LocalDateTime start;
    LocalDateTime end;
    boolean online;
    String location;
    String scheduleId;

    @Column(name = "ac_calculated")
    boolean acCalculated =false;

    public DingTalkSchedulePo(String summary, LocalDateTime start, LocalDateTime end, boolean online, String location) {
        this.summary = summary;
        this.start = start;
        this.end = end;
        this.online = online;
        this.location = location;
    }
    public void update(String summary, LocalDateTime start, LocalDateTime end, boolean online, String location) {
        this.summary = summary;
        this.start = start;
        this.end = end;
        this.online = online;
        this.location = location;
    }
}
