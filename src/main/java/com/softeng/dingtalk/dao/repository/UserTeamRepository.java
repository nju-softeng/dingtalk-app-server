package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.entity.UserTeam;
import com.softeng.dingtalk.entity.cpk.UserTeamCPK;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LiXiaoKang
 * @description 操作UserTeam实体类的接口
 * @date 2/5/2023
 */

@Repository
public interface UserTeamRepository extends CustomizedRepository<UserTeam, UserTeamCPK>{

    /**
     * 获取用户所在的所有用户组
     * @param userId 用户id
     * @return 用户所在的所有用户组
     */
    List<UserTeam> findAllByUserId(int userId);

    void deleteAllByUserId(int userId);
}
