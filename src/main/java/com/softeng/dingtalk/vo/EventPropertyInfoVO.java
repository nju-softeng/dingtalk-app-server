package com.softeng.dingtalk.vo;

import lombok.Data;

@Data
public class EventPropertyInfoVO {
    int id;
    String eventName;
    String year;
    String type;
    String path;
    public EventPropertyInfoVO(int id, String eventName, String year, String type) {
        this.id = id;
        this.eventName = eventName;
        this.year = year;
        this.type = type;
    }
}
