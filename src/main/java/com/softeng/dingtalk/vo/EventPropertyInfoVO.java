package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventPropertyInfoVO {
    int id;
    String name;
    String year;
    String type;
    String path;
    public EventPropertyInfoVO(int id, String name, String year, String type) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.type = type;
    }
}
