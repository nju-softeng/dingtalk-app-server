package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.DcRecord;

import com.softeng.dingtalk.projection.DcRecordProjection;
import com.softeng.dingtalk.vo.AppliedVO;
import com.softeng.dingtalk.vo.CheckedVO;
import com.softeng.dingtalk.vo.ToCheckVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


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


    /**
     * 更具分页查出的id去查询
     * @param ids
     * @return java.util.List<com.softeng.dingtalk.projection.DcRecordProjection>
     * @Date 1:30 PM 2/10/2020
     **/
    @EntityGraph(value="dcRecord.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select d from DcRecord d where d.id in :ids order by d.id desc")
    List<DcRecordProjection> findAllById(List<Integer> ids);


    /**
     * 分页查询出申请者的申请id
     * @param uid, pageable
     * @return java.util.List<java.lang.Integer>
     * @Date 3:08 PM 2/10/2020
     **/
    @Query("select d.id from DcRecord d where d.applicant.id = :uid")
    List<Integer> listIdByUid(@Param("uid") int uid, Pageable pageable);


    /**
     * 根据审核人的id获取已经审核的申请
     * @param uid
     * @return java.util.List<com.softeng.dingtalk.entity.DcRecord>
     * @Date 7:49 PM 1/28/2020
     **/
    @Query("select new com.softeng.dingtalk.vo.CheckedVO(d.id, d.applicant.name, d.applicant.id, d.dvalue, d.cvalue, d.dc, d.ac, d.yearmonth, d.week, d.insertTime, d.weekdate) " +
            "from DcRecord d where d.auditor.id = :uid and d.status = true order by d.id desc")
    List<CheckedVO> listChecked(@Param("uid")int uid);




    /**
     * 审核人查看待审核的申请  ->  根据uid(审核人)，获得待审核的申请
     * @param uid  审核人id
     * @return java.util.List<com.softeng.dingtalk.vo.ApplicationVO>
     * @Date 8:18 PM 1/19/2020
     **/
    @Query("select new com.softeng.dingtalk.vo.ToCheckVO(d.id, d.applicant.id, d.applicant.name, d.dvalue, d.yearmonth, d.week, d.insertTime, d.weekdate) from DcRecord d where d.auditor.id = :uid and d.status = false order by d.id desc")
    List<ToCheckVO> listToCheckVO(@Param("uid") int uid);


    /**
     * 计算项目期间累计dc值
     * @param uid, id, stime, etime
     * @return java.lang.Double
     * @Date 6:51 PM 1/17/2020
     **/
    @Query(value = "select ifnull((select sum(dc) from dc_record where applicant_id = :uid and auditor_id = :aid and weekdate >= :stime and weekdate <= :etime), 0)", nativeQuery = true)
    Double getByTime(@Param("uid") int uid, @Param("aid") int id, @Param("stime") LocalDate stime, @Param("etime") LocalDate etime);

    @Query(value = "select dc, CONCAT(yearmonth, '-',`week`) as 'date' from dc_record where applicant_id = :uid and auditor_id = :aid and weekdate >= :stime and weekdate <= :etime", nativeQuery = true)
    List<Map<String, Object>> getDcBytime(@Param("uid") int uid, @Param("aid") int id, @Param("stime") LocalDate stime, @Param("etime") LocalDate etime);


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
     *
     **/
    @Query("select new com.softeng.dingtalk.vo.AppliedVO(d.id, d.auditor.id, d.auditor.name, d.status, d.dvalue, d.dc, d.ac, d.yearmonth, d.week, d.weekdate, d.insertTime) " +
            "from DcRecord d where d.applicant.id = :uid")
    List<AppliedVO> listByUid(@Param("uid") int uid, Pageable pageable);


    /**
     * 申请人申请的dcRecord数目
     * @param uid
     * @return java.lang.Integer
     * @Date 2:19 PM 1/28/2020
     **/
    @Query("select count (d) from DcRecord d where d.applicant.id = :uid")
    Integer getCountByUid(@Param("uid") int uid);


    /**
     * 获取 dc_record 的指定用户所在日期，周，所有dc值之和（即包括其他审核人审核的dc值）
     * @param uid, yearmonth, week
     * @return java.lang.Double
     * @Date 8:34 PM 1/2/2020
     **/
    @Query(value = "select sum(dc) from dc_record where applicant_id = :uid and yearmonth = :yearmonth and week = :week",
            nativeQuery = true)
    Double getUserWeekTotalDc(@Param("uid") int uid, @Param("yearmonth") int yearmonth, @Param("week") int week);





}
