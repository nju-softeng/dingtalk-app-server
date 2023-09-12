package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.UserTeamReq;
import com.softeng.dingtalk.dto.resp.UserTeamResp;
import com.softeng.dingtalk.entity.UserTeam;
import org.springframework.stereotype.Component;

@Component
public class UserTeamConvertor extends AbstractConvertorTemplate<UserTeamReq, UserTeamResp, UserTeam> {
}
