package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class DingTalkSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User organizer;

    @OneToMany(mappedBy = "dingTalkSchedule",cascade = {CascadeType.REMOVE})
    List<AbsentOA> absentOAList;

    @OneToMany(mappedBy = "dingTalkSchedule",cascade = CascadeType.ALL)
    List<DingTalkScheduleDetail> dingTalkScheduleDetailList;
    //标题
    String summary;
    LocalDate start;
    LocalDate end;
    boolean isOnline;
    String location;
    String scheduleId;

    @Column(name = "isAcCalculated")
    boolean isAcCalculated =false;

    public DingTalkSchedule(String summary, LocalDate start, LocalDate end, boolean isOnline, String location) {
        this.summary = summary;
        this.start = start;
        this.end = end;
        this.isOnline = isOnline;
        this.location = location;
    }
    public void update(String summary, LocalDate start, LocalDate end, boolean isOnline, String location) {
        this.summary = summary;
        this.start = start;
        this.end = end;
        this.isOnline = isOnline;
        this.location = location;
    }
}
