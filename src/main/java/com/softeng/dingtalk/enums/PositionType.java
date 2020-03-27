package com.softeng.dingtalk.enums;

public enum PositionType {
    OTHER("其他"),
    UNDERGRADUATE("本科生"),
    POSTGRADUATE("硕士生"),
    DOCTOR("博士生");

    private String type;

    public String getName() {
        return type;
    }

    PositionType(String type) {
        this.type = type;
    }

}
