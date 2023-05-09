package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.dto.CommonResult;
import com.softeng.dingtalk.dto.req.UserPermissionReq;
import com.softeng.dingtalk.dto.req.UserTeamReq;
import com.softeng.dingtalk.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v2/userTeam")
public class UserTeamController {
    @Resource
    private UserTeamService userTeamService;

    @PostMapping("")
    public CommonResult<String> addUserTeam(@RequestBody UserTeamReq userTeamReq) {
        userTeamService.addUserTeam(userTeamReq);
        return CommonResult.success("添加研究组成功");
    }

    @PostMapping("/batch")
    public CommonResult<String> UpdateUserPermissionListBatch(@RequestBody List<UserTeamReq> userTeamReqList) {
        userTeamService.updateUserPermissionList(userTeamReqList);
        return CommonResult.success("批量更新研究组成功");
    }
}
