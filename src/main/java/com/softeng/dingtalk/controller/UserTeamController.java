package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.dto.CommonResult;
import com.softeng.dingtalk.dto.req.UserTeamReq;
import com.softeng.dingtalk.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/api/v2/userTeam")
public class UserTeamController {
    @Resource
    private UserTeamService userTeamService;

    @PostMapping("")
    public CommonResult<String> addUserTeam(UserTeamReq userTeamReq) {
        userTeamService.addUserTeam(userTeamReq);
        return CommonResult.success("添加成功");
    }
}
