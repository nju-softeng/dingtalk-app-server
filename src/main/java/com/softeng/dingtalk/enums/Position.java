package com.softeng.dingtalk.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author zhanyeye
 * @description 学位
 * @create 5/28/2020 8:20 AM
 */

public enum Position {
    DOCTOR("博士生"),
    ACADEMIC("学硕"),
    PROFESSIONAL("专硕"),
    UNDERGRADUATE("本科生"),
    OTHER("待定"),

    /**
     * @author LiXiaoKang
     * @description todo-新增职位/学位
     * @create 2/4/2023
     */
    TEACHER("教师");

    private String title;

    private Position(String title) {
        this.title = title;
    }

    @JsonValue
    public String getTitle() {
        return title;
    }

}
