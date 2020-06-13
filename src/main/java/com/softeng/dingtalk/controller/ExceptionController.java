package com.softeng.dingtalk.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Set;

/**
 * @author zhanyeye
 * @description
 * @create 3/5/2020 5:09 PM
 */
@Slf4j
@RestControllerAdvice
public class ExceptionController {

    /**
     * 属性校验失败异常
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)  //MethodArgumentNotValidException 校验失败异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handleValidException(MethodArgumentNotValidException exception) {
        StringBuilder strBuilder = new StringBuilder();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(e -> {
                    strBuilder.append(e.getField());
                    strBuilder.append(":");
                    strBuilder.append(e.getDefaultMessage());
                    strBuilder.append(";");
                });
        return Map.of("message", strBuilder.toString());
    }


    /**
     * jackson json类型转换异常
     * @param exception
     * @return
     */
    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handleInvalidFormatException(InvalidFormatException exception) {
        StringBuilder strBuilder = new StringBuilder();
        exception.getPath()
                .forEach(p -> {
                    strBuilder.append("类型错误! ");
                    strBuilder.append("属性");
                    strBuilder.append(p.getFieldName());
                    strBuilder.append(",您输入的值：" + exception.getValue());

                });
        return Map.of("message", strBuilder.toString());
    }


    /**
     * 方法级参数校验失败异常
     * @param exception
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handleConstraintViolationException(ConstraintViolationException exception) {
        StringBuilder strBuilder = new StringBuilder();
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        violations.forEach(v -> strBuilder.append(v.getMessage() + "; "));
        return Map.of("message", strBuilder.toString());
    }
}