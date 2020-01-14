package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.DcRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description  操作 DcRecord 实体类的接口
 * @date 12/12/2019
 */
@Repository
public interface DcRecordRepository extends JpaRepository<DcRecord, Integer> {

    @Query(value = "select ifnull((select sum(dc) from dc_record where applicant_id = :uid and auditor_id = :aid and insert_time >= :stime and insert_time <= :etime), 0)", nativeQuery = true)
    Double getByTime(@Param("uid") int uid, @Param("aid") int id, @Param("stime") String stime, @Param("etime") String etime);

    /**
     * 更具 id 查询指定ID的  applicant.id, yearmonth, week
     * @param id
     * @return com.softeng.dingtalk.entity.DcRecord
     * @Date 7:17 PM 1/4/2020
     **/
    @Query("select new com.softeng.dingtalk.entity.DcRecord(d.applicant.id, d.yearmonth, d.week) from DcRecord d where d.id = :id")
    DcRecord getbyId(@Param("id") int id);


    /**
     * 审核人更新dc申请的记录
     * @param id, cvalue, dc
     * @return void
     * @Date 10:58 AM 1/1/2020
     **/
    @Modifying
    @Query("update DcRecord d set d.cvalue = :cvalue, d.dc = :dc, d.ischeck = true where d.id = :id")
    void updateById(@Param("id") int id, @Param("cvalue") double cvalue, @Param("dc") double dc);

    /**
     * 查询是否存在某条记录，
     * @param uid, aid, yearmonth, week
     * @return java.lang.Integer
     * @Date 7:47 PM 12/30/2019
     **/
    @Query(value =
            "SELECT IfNULL((SELECT id FROM dc_record WHERE applicant_id = :uid and auditor_id = :aid and yearmonth = :yearmonth and week = :week LIMIT 1), 0)",
            nativeQuery = true)
    Integer isExist(@Param("uid") int uid,@Param("aid") int aid, @Param("yearmonth") int yearmonth, @Param("week") int week);

    /**
     * 用于分页显示申请历史 ->  根据uid(用户)，获取用户提交的申请，实现分页
     * @param uid 申请人ID
     * @param pageable
     * @return java.util.List<com.softeng.dingtalk.entity.DcRecord>
     * @Date 4:28 PM 12/30/2019
     **/
    @Query("select d from DcRecord d where d.applicant.id = :uid")
    List<DcRecord> listByUid(@Param("uid") int uid, Pageable pageable);

    /**
     * 审核人查看待审核的申请  ->  根据uid(审核人)，获得待审核的申请
     * @param uid  审核人id
     * @return java.util.List<com.softeng.dingtalk.entity.DcRecord>
     * @Date 4:36 PM 12/30/2019
     **/
    @Query("select d from DcRecord d where d.auditor.id = :uid and d.ischeck = false")
    List<DcRecord> listPendingReview(@Param("uid") int uid);


    /**
     * 获取指定 dc_record 的用户所在日期，周，所有dc值之和（即包括其他审核人审核的dc值）
     * @param id  DcRecord id
     * @return java.lang.Double
     * @Date 8:34 PM 1/2/2020
     **/
    @Query(value = "select sum(dc) from dc_record where applicant_id = :uid and yearmonth = :yearmonth and week = :week",
            nativeQuery = true)
    Double getUserWeekTotalDc(@Param("uid") int uid, @Param("yearmonth") int yearmonth, @Param("week") int week);
}
