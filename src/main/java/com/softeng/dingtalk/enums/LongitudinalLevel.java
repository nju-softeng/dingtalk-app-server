package com.softeng.dingtalk.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Description 纵向项目级别
 * @Author Jerrian Zhao
 * @Data 01/28/2022
 */
public enum LongitudinalLevel {
    NATIONAL("国家级项目"),
    PROVINCIAL("省部级项目"),
    BUREAU("厅局级项目"),
    NATIONAL_INSTITUTE("国家级学会和协会项目"),
    PROVINCIAL_INSTITUTE("省级学会和协会项目");

    private String title;

    private LongitudinalLevel(String title) {
        this.title = title;
    }

    @JsonValue
    public String getTitle() {
        return title;
    }
}
