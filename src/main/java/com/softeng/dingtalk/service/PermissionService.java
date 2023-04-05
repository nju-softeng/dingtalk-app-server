package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.convertor.PermissionConvertor;
import com.softeng.dingtalk.dao.repository.PermissionRepository;
import com.softeng.dingtalk.dao.repository.UserPermissionRepository;
import com.softeng.dingtalk.dto.resp.PermissionResp;
import com.softeng.dingtalk.po_entity.UserPermission;
import com.softeng.dingtalk.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
@Slf4j
public class PermissionService {
    @Resource
    private UserPermissionRepository userPermissionRepository;
    @Resource
    private PermissionRepository permissionRepository;
    @Resource
    PermissionConvertor permissionConvertor;
    /**
     * 获得该用户的所有权限名
     * @param userId
     * @return 所有权限名
     */
    public List<PermissionResp> getPermissions(int userId){
        List<UserPermission> userPermissionList = userPermissionRepository.findAllByUserId(userId);
        return StreamUtils.map(
                userPermissionList,
                userPermission -> permissionConvertor.entity_PO2Resp(permissionRepository.findById(userPermission.getPermissionId()))
        );
    }
}
