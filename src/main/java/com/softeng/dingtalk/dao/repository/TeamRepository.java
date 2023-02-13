package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.TeamPo;
import org.springframework.stereotype.Repository;

/**
 * @author LiXiaoKang
 * @description 操作Team表类的接口
 * @date 2/5/2023
 */

@Repository
public interface TeamRepository extends CustomizedRepository<TeamPo, Integer>{
    TeamPo findById(int id);
}
