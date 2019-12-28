package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Application;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    /**
     * 用于分页显示申请历史 ->  根据uid(用户)，获取用户提交的申请，实现分页
     * @param uid 申请人id
     * @param pageable
     * @return
     */
    @Query("select a from Application a where a.applicant.id = :uid")
    List<Application> listApplicationByuid(@Param("uid") int uid, Pageable pageable);


    /**
     * 审核人查看待审核的申请  ->  根据uid(审核人)，获得待审核的申请
     * @param uid 审核人id
     * @return List<Application>
     */
    @Query("select a from Application a where a.auditor.id = :uid and a.ischeck = false")
    List<Application> listAuditorPendingApplication(@Param("uid") int uid);


    /**
     * 更新申请状态为已审核
     * @param aid 申请id
     * @return void
     * @Date 7:36 PM 12/27/2019
     **/
    @Modifying
    @Query("update Application a set a.ischeck = true where a.id = :aid")
    void updateApplicationCheckStatus(@Param("aid") int aid);



//    //todo 可能是多余的
//    /**
//     * 根据uid(申请人)，获得待审核的申请  ->  申请人查看待审核的申请
//     * @param uid 申请人id
//     * @return
//     */
//    @Query("select a from Application a where a.applicant.id = :uid and a.ischeck = false")
//    List<Application> listApplicantPendingApplication(@Param("uid") int uid);

}
