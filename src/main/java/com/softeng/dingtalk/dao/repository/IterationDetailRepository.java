package com.softeng.dingtalk.dao.repository;


import com.softeng.dingtalk.po.IterationDetailPo;
import com.softeng.dingtalk.po.UserPo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 3/14/2020
 */
public interface IterationDetailRepository extends CustomizedRepository<IterationDetailPo, Integer> {

    /**
     * 删除Iteration 的所有 iterationDetail
     * @param id 主键
     **/
    void deleteByIterationId(int id);


    @Query("select itd.user from IterationDetailPo itd where itd.iteration.id = :id")
    List<UserPo> listUserByIterationId(@Param("id") int id);


    /**
     * 查询用户所参与迭代的id
     * @param uid
     * @return
     */
    @Query("select itd.iteration.id from IterationDetailPo itd where itd.user.id = :uid")
    List<Integer> listIterationIdByUid(@Param("uid") int uid);


}
