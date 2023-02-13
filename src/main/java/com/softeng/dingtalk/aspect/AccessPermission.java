package com.softeng.dingtalk.aspect;

import com.softeng.dingtalk.enums.PermissionEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定权限通行
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessPermission {
    PermissionEnum value();
}
