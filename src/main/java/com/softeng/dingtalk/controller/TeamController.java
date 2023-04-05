package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.UserContextHolder;
import com.softeng.dingtalk.dto.CommonResult;
import com.softeng.dingtalk.dto.resp.TeamResp;
import com.softeng.dingtalk.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v2/team")
public class TeamController {

    @Resource
    TeamService teamService;
    @Resource
    UserContextHolder userContextHolder;

    @GetMapping("")
    public CommonResult<List<TeamResp>> getPersonalTeams(){
        return CommonResult.success(teamService.getTeams(userContextHolder.getUserContext().getUid()));
    }

    @GetMapping("/{uid}")
    public CommonResult<List<TeamResp>> getOthersTeams(@PathVariable int uid){
        return CommonResult.success(teamService.getTeams(uid));
    }
}
