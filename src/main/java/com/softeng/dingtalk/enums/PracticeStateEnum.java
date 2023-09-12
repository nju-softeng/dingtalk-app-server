package com.softeng.dingtalk.enums;

import lombok.Getter;

@Getter
public enum PracticeStateEnum {
    REJECTED(-1), AUDITING(0), ACCEPTED(1);

    private int value;

    PracticeStateEnum(int value) {
        this.value = value;
    }
}
