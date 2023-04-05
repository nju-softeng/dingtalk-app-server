package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.UserContextHolder;
import com.softeng.dingtalk.dto.CommonResult;
import com.softeng.dingtalk.dto.resp.PermissionResp;
import com.softeng.dingtalk.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v2/permission")
public class PermissionController {

    @Resource
    PermissionService permissionService;
    @Resource
    UserContextHolder userContextHolder;
    /**
     * 获取用户自身的权限信息
     * @return
     */
    @GetMapping("")
    public CommonResult<List<PermissionResp>> getPersonalPermissions(){
        return CommonResult.success(permissionService.getPermissions(userContextHolder.getUserContext().getUid()));
    }

    /**
     * 获取其他用户权限信息
     * @param uid
     * @return
     */
    @GetMapping("/{uid}")
    public CommonResult<List<PermissionResp>> getOthersPermissions(@PathVariable int uid){
        return CommonResult.success(permissionService.getPermissions(uid));
    }
}
