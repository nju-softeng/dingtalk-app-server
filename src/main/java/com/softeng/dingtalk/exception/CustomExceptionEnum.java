package com.softeng.dingtalk.exception;

public enum CustomExceptionEnum {
    PARAM_NOT_NULL(100000, "参数不应该为空"),
    ENUM_NOT_FOUND(100001, "枚举异常，找不到该类型"),
    FIELD_CONVERT_ERROR(100002, "字段转化异常"),
    PERMISSION_NOT_FOUND(100003, "没有访问权限"),
    LOGIN_PLEASE(100004, "未登录"),
//    PASSWORD_ERROR(100005, "密码错误"),
    ACCESS_DENIED(100006, "权限不足，拒绝访问"),
    ;

    private static final int COMMON_EXCEPTION_CODE_BASE = 100000;

    private final int code;
    private final String msg;
    CustomExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private boolean isCommonException() {
        return code >= COMMON_EXCEPTION_CODE_BASE && code < 200000;
    }

    public RuntimeException toException() {
        return toException(msg);
    }

    private RuntimeException toException(String msg) {
        if (isCommonException()) return new CommonException(code, msg);
        return new RuntimeException("编码异常，联系开发");
    }

    public void throwIf(boolean condition) {
        if (condition) {
            throw toException();
        }
    }

    public void throwWithMessage(String msg) {
        throw toException(msg);
    }

    public void throwDirectly() {
        throw toException();
    }
}
