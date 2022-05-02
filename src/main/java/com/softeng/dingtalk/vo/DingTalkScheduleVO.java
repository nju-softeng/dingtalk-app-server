package com.softeng.dingtalk.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DingTalkScheduleVO {
    Integer id;
    int organizerId;
    List<Integer> attendeesIdList;
    String summary;
    LocalDateTime start;
    LocalDateTime end;
    boolean online;
    String location;
}
