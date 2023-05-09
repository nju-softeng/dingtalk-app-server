package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.dto.CommonResult;
import com.softeng.dingtalk.dto.req.UserPermissionReq;
import com.softeng.dingtalk.dto.req.UserTeamReq;
import com.softeng.dingtalk.po_entity.UserPermission;
import com.softeng.dingtalk.service.UserPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v2/userPermission")
public class UserPermissionController {

    @Resource
    private UserPermissionService userPermissionService;

    @PostMapping("")
    public CommonResult<String> addUserPermission(@RequestBody UserPermissionReq userPermissionReq) {
        userPermissionService.addUserPermission(userPermissionReq);
        return CommonResult.success("添加权限成功");
    }

    @PostMapping("/batch")
    public CommonResult<String> UpdateUserPermissionListBatch(@RequestBody List<UserPermissionReq> userPermissionReqList) {
        userPermissionService.updateUserPermissionList(userPermissionReqList);
        return CommonResult.success("批量更新权限成功");
    }
}
