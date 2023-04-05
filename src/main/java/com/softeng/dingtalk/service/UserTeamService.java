package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.convertor.UserTeamConvertor;
import com.softeng.dingtalk.dao.repository.UserTeamRepository;
import com.softeng.dingtalk.dto.req.UserTeamReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class UserTeamService {
    @Resource
    private UserTeamRepository userTeamRepository;
    @Resource
    private UserTeamConvertor userTeamConvertor;

    public void addUserTeam(UserTeamReq userTeamReq) {
        userTeamRepository.save(userTeamConvertor.req2Entity_PO(userTeamReq));
    }
}
