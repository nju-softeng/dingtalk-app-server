package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.PermissionPo;
import org.springframework.stereotype.Repository;

/**
 * @author LiXiaoKang
 * @description 操作Permission实体类的接口
 * @date 2/5/2023
 */

@Repository
public interface PermissionRepository extends CustomizedRepository<PermissionPo, Integer>{

    PermissionPo findById(int id);
}
