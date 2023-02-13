package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.convertor.AbstractConvertorTemplate;
import com.softeng.dingtalk.dto.req.TeamReq;
import com.softeng.dingtalk.dto.resp.TeamResp;
import com.softeng.dingtalk.entity.Team;
import com.softeng.dingtalk.po.TeamPo;
import org.springframework.stereotype.Component;

@Component
public class TeamConvertor extends AbstractConvertorTemplate<TeamReq, TeamResp, Team, TeamPo> {
}
