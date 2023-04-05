package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.dto.CommonResult;
import com.softeng.dingtalk.dto.req.UserPermissionReq;
import com.softeng.dingtalk.dto.req.UserTeamReq;
import com.softeng.dingtalk.service.UserPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/api/v2/userPermission")
public class UserPermissionController {

    @Resource
    private UserPermissionService userPermissionService;

    @PostMapping("")
    public CommonResult<String> addUserPermission(UserPermissionReq userPermissionReq) {
        userPermissionService.addUserPermission(userPermissionReq);
        return CommonResult.success("添加成功");
    }
}
