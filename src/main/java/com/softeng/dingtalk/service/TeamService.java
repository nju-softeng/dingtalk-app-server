package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.convertor.TeamConvertor;
import com.softeng.dingtalk.dao.repository.TeamRepository;
import com.softeng.dingtalk.dao.repository.UserTeamRepository;
import com.softeng.dingtalk.dto.resp.TeamResp;
import com.softeng.dingtalk.entity.UserTeam;
import com.softeng.dingtalk.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
@Slf4j
public class TeamService {
    @Resource
    private TeamRepository teamRepository;
    @Resource
    private UserTeamRepository userTeamRepository;
    @Resource
    TeamConvertor teamConvertor;
    /**
     * 获得该用户所在的所有用户组名
     * @param userId
     * @return
     */
    public List<TeamResp> getTeams(int userId){
        List<UserTeam> userTeamList = userTeamRepository.findAllByUserId(userId);
        return StreamUtils.map(
                userTeamList,
                userTeam -> teamConvertor.entity2Resp(teamRepository.findById(userTeam.getTeamId()))
        );
    }
}
