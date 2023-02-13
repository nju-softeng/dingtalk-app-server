package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.UserPermissionPo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LiXiaoKang
 * @description 操作UserPermission实体类的接口
 * @date 2/5/2023
 */

@Repository
public interface UserPermissionRepository extends CustomizedRepository<UserPermissionPo, UserPermissionRepository>{

    /**
     * 获取用户的所有权限
     * @param userId 用户id
     * @return 用户所有权限
     */
    List<UserPermissionPo> findAllByUserId(int userId);
}
