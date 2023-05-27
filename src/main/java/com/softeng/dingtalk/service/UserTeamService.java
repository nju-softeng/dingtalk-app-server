package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.convertor.UserTeamConvertor;
import com.softeng.dingtalk.dao.repository.UserTeamRepository;
import com.softeng.dingtalk.dto.req.UserTeamReq;
import com.softeng.dingtalk.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserTeamService {
    @Resource
    private UserTeamRepository userTeamRepository;
    @Resource
    private UserTeamConvertor userTeamConvertor;

    public void addUserTeam(UserTeamReq userTeamReq) {
        userTeamRepository.save(userTeamConvertor.req2Entity(userTeamReq));
    }

    public void updateUserPermissionList(List<UserTeamReq> userTeamReqList) {
        int userId = userTeamReqList.get(0).getUserId();
        userTeamRepository.deleteAllByUserId(userId);
        userTeamRepository.saveBatch(StreamUtils.map(userTeamReqList,
                (userTeamReq -> userTeamConvertor.req2Entity(userTeamReq))));
    }
}
