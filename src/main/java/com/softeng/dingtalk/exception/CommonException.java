package com.softeng.dingtalk.exception;

import lombok.Getter;

/**
 * @Author: lilingj
 * @CreateTime: 2023-01-10  14:51
 * @Description: 通用异常，错误码范围: [100000, 200000)
 * @Version: 1.0
 */

public class CommonException extends RuntimeException {

    @Getter
    private Integer code;

    public CommonException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

}
