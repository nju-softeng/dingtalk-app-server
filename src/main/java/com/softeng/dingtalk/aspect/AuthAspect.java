package com.softeng.dingtalk.aspect;

import com.softeng.dingtalk.component.UserContextHolder;
import com.softeng.dingtalk.exception.CustomExceptionEnum;
import org.aspectj.lang.annotation.Before;

import javax.annotation.Resource;

public class AuthAspect {

    @Resource
    private UserContextHolder userContextHolder;

    @Before("@annotation(accessPermission)")
    public void checkPermission(AccessPermission accessPermission) {
        for (Integer permissionId : userContextHolder.getUserContext().getPermissionIds()) {
            if (accessPermission.value().getCode() == permissionId) {
                return;
            }
        }
        CustomExceptionEnum.ACCESS_DENIED.throwWithMessage("需要权限：" + accessPermission.value().getName());
    }

}
