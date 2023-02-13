package com.softeng.dingtalk.exception;

import com.softeng.dingtalk.dto.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: lilingj
 * @CreateTime: 2023-01-14  19:31
 * @Description: 通用全局异常捕捉
 * @Version: 1.0
 */

@RestControllerAdvice
@Slf4j
public class CommonExceptionConfig {

    @ExceptionHandler(CommonException.class)
    public CommonResult<String> handleCommonException(CommonException e) {
        log.error("CommonException", e);
        return CommonResult.fail(e);
    }

    @ExceptionHandler(RuntimeException.class)
    public CommonResult<String> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException", e);
        return CommonResult.fail(e);
    }

    @ExceptionHandler(Exception.class)
    public CommonResult<String> handleException(Exception e) {
        log.error("Exception", e);
        return CommonResult.fail(e);
    }

}
