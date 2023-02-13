package com.softeng.dingtalk.enums;

import com.softeng.dingtalk.exception.CustomExceptionEnum;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PermissionEnum {

    /**
     * 超级管理员的特有权限
     */
    APPOINT_REVIEWER(1, "任命审核人", "有权利任命实习申请、专利申请、报销申请、虚拟机申请的审核人"),
    SET_INTERNSHIP_ALLOWED_PERIOD(2, "设置实习期", "有权利设置实习允许时间段"),
    EDIT_ANY_USER_INFO(3, "编辑用户信息", "有权利修改任意用户信息（在读学位/学号/用户权限）"),

    REVIEW_INTERNSHIP_APPLICATION(11, "实习申请审核人", "有权利通过/退回实习申请"),
    REVIEW_PATENT_APPLICATION(12, "专利申请审核人", "有权利通过/退回专利申请"),
    REVIEW_REIMBURSE_APPLICATION(13, "报销申请审核人", "有权利通过/退回报销申请"),
    REVIEW_VM_DEVICE_APPLICATION(14, "虚拟机申请审核人", "有权利通过/退回虚拟机申请")
    ;

//    @Getter
    private final int code;
//    @Getter
    private final String name;
//    @Getter
    private final String description;

    PermissionEnum(int code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public static PermissionEnum fromCode(int code) {
        return Arrays.stream(PermissionEnum.values())
                .filter(permissionEnum -> permissionEnum.getCode() == code)
                .findAny()
                .orElseThrow(CustomExceptionEnum.ENUM_NOT_FOUND::toException);
    }
}
