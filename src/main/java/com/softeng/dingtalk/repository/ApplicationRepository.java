package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Application;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 12/11/2019
 */
@Repository
public interface ApplicationRepository extends CustomizedRepository<Application, Integer>, JpaRepository<Application, Integer> {

    //更具uid(用户)，获取用户提交的申请，实现分页 ->  用于显示申请历史
    @Query("select a from Application a where a.applicant.id = :uid")
    List<Application> listApplicationByuid(@Param("uid") int uid, Pageable pageable);


    //根据uid(审核人)，获得待审核的申请  ->  审核人查看待审核的申请
    @Query("select a from Application a where a.auditor.id = :uid and a.ischeck = false")
    List<Application> listPendingApplication(@Param("uid") int uid);

}
