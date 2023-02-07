package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Team;
import org.springframework.stereotype.Repository;

/**
 * @author LiXiaoKang
 * @description 操作Team实体类的接口
 * @date 2/5/2023
 */

@Repository
public interface TeamRepository extends CustomizedRepository<Team, Integer>{
    Team findById(int id);
}
