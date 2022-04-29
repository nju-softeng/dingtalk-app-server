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

    @OneToOne
    @JoinColumn(name = "user_id")
    User organizer;

    @ManyToMany
    List<User> attendees;
    //标题
    String summary;
    LocalDate start;
    LocalDate end;
    boolean isOnline;
    String location;
    String scheduleId;
}
