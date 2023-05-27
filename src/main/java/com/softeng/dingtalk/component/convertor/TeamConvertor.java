package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.TeamReq;
import com.softeng.dingtalk.dto.resp.TeamResp;
import com.softeng.dingtalk.entity.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamConvertor extends AbstractConvertorTemplate<TeamReq, TeamResp, Team> {
}
