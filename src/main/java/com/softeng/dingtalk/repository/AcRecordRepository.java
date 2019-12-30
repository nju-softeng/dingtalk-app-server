package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 12/27/2019
 */
@Repository
public interface AcRecordRepository extends CustomizedRepository<AcRecord, Integer>, JpaRepository<AcRecord, Integer> {

    /**
     * 审核人获取自己审核的AC日志
     * @param uid 审核人id
     * @return java.util.List<com.softeng.dingtalk.entity.AcRecord>
     * @Date 8:44 PM 12/27/2019
     **/
    @Query("select a from AcRecord a where a.auditor.id = :uid")
    List<AcRecord> listByAuditorId(@Param("uid") int uid);

    /**
     * 申请人获取自己的AC日志
     * @param uid 申请人id
     * @return java.util.List<com.softeng.dingtalk.entity.AcRecord>
     * @Date 8:45 PM 12/27/2019
     **/
    @Query("select a from AcRecord a where a.user.id = :uid")
    List<AcRecord> listByApplicantId(@Param("uid") int uid);
}
