package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.convertor.UserPermissionConvertor;
import com.softeng.dingtalk.dao.repository.UserPermissionRepository;
import com.softeng.dingtalk.dto.req.UserPermissionReq;
import com.softeng.dingtalk.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserPermissionService {
    @Resource
    private UserPermissionRepository userPermissionRepository;
    @Resource
    private UserPermissionConvertor userPermissionConvertor;

    public void addUserPermission(UserPermissionReq userPermissionReq) {
        userPermissionRepository.save(userPermissionConvertor.req2Entity(userPermissionReq));
    }

    public void updateUserPermissionList(List<UserPermissionReq> userPermissionReqList) {
        int userId = userPermissionReqList.get(0).getUserId();
        userPermissionRepository.deleteAllByUserId(userId);
        userPermissionRepository.saveBatch(StreamUtils.map(userPermissionReqList,
                (userPermissionReq -> userPermissionConvertor.req2Entity(userPermissionReq))));
    }

}
