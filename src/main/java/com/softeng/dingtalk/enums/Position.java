package com.softeng.dingtalk.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;


@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Position {
    DOCTOR("博士生"),
    POSTGRADUATE("研究生"),
    UNDERGRADUATE("本科生"),
    OTHER("其他");
    private String title;
    private Position(String title) {
        this.title = title;
    }
}
