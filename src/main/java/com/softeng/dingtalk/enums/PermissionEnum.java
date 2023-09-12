package com.softeng.dingtalk.enums;

import com.softeng.dingtalk.exception.CustomExceptionEnum;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PermissionEnum {

    /**
     * 超级管理员的特有权限
     */
    ALLOCATE_PERMISSION(1, "分配管理权限", "有权利分配各种权限"),
    SET_INTERNSHIP_ALLOWED_PERIOD(2, "设置实习期", "有权利设置实习允许时间段"),
    EDIT_ANY_USER_INFO(3, "编辑用户信息", "有权利修改任意用户信息（在读学位/学号/用户权限）"),
    EDIT_SCROLL_NEWS_BOARD(4, "编辑滚动公告栏", "有权利编辑滚动公告栏"),

    REVIEW_INTERNSHIP_APPLICATION(5, "实习申请审核", "有权利通过/退回实习申请"),
    REVIEW_PATENT_APPLICATION(6, "专利申请审核", "有权利通过/退回专利申请"),
    REVIEW_REIMBURSE_APPLICATION(7, "报销申请审核", "有权利通过/退回报销申请"),
    REVIEW_VM_DEVICE_APPLICATION(8, "虚拟机申请审核", "有权利通过/退回虚拟机申请"),
    REVIEW_PERFORMANCE_APPLICATION(9, "绩效申请审核", "有权利通过/退回绩效申请审核"),
    REVIEW_PAPER_APPLICATION(10, "论文申请审核", "有权利设置论文的录用结果"),
    EDIT_STANDARD_PARAMETER(11, "编辑标准参数", "有权利编辑标准参数")
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
