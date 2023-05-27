package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.UserReq;
import com.softeng.dingtalk.dto.resp.UserResp;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.service.PermissionService;
import com.softeng.dingtalk.service.TeamService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserConvertor extends AbstractConvertorTemplate<UserReq, UserResp, User> {

    @Resource
    private PermissionService permissionService;
    @Resource
    private TeamService teamService;

    @Override
    public UserResp entity2Resp(User user) {
        return super.entity2Resp(user)
                .setPermissionList(permissionService.getPermissions(user.getId()))
                .setTeamList(teamService.getTeams(user.getId()));
    }
}
