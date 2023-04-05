package com.softeng.dingtalk.enums;

import lombok.Getter;

@Getter
public enum NewsState {
    IS_SHOWN(1),
    IS_NOT_SHOWN(0),
    IS_DELETED(1),
    IS_NOT_DELETED(0)
    ;
    private int value;


    NewsState(int value) {
        this.value = value;
    }
}
