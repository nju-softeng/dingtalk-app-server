package com.softeng.pms.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author zhanyeye
 * @description 学位
 * @create 5/28/2020 8:20 AM
 */

public enum Position {
    DOCTOR("博士生"),
    POSTGRADUATE("硕士生"),
    UNDERGRADUATE("本科生"),
    OTHER("待定");

    private String title;

    private Position(String title) {
        this.title = title;
    }

    @JsonValue
    public String getTitle() {
        return title;
    }

}
