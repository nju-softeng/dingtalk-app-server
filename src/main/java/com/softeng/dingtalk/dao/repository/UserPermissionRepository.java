package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.entity.UserPermission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LiXiaoKang
 * @description 操作UserPermission实体类的接口
 * @date 2/5/2023
 */

@Repository
public interface UserPermissionRepository extends CustomizedRepository<UserPermission, UserPermissionRepository>{

    /**
     * 获取用户的所有权限
     * @param userId 用户id
     * @return 用户所有权限
     */
    List<UserPermission> findAllByUserId(int userId);


    void deleteAllByUserId(int userId);
}
