package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.UserTeamPo;
import com.softeng.dingtalk.po.cpk.UserTeamCPK;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LiXiaoKang
 * @description 操作UserTeam实体类的接口
 * @date 2/5/2023
 */

@Repository
public interface UserTeamRepository extends CustomizedRepository<UserTeamPo, UserTeamCPK>{

    /**
     * 获取用户所在的所有用户组
     * @param userId 用户id
     * @return 用户所在的所有用户组
     */
    List<UserTeamPo> findAllByUserId(int userId);
}
