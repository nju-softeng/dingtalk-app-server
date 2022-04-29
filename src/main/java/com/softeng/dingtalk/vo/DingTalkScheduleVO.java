package com.softeng.dingtalk.vo;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DingTalkScheduleVO {
    Integer id;
    int organizerId;
    List<Integer> attendeesIdList;
    String summary;
    LocalDate start;
    LocalDate end;
    boolean isOnline;
    String location;
}
