package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.AcRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @date 12/27/2019
 */
@Repository
public interface AcRecordRepository extends JpaRepository<AcRecord, Integer> {

    // todo 未被使用
    // 审核人获取自己审核的AC日志
    @Query("select a from AcRecord a where a.auditor.id = :uid")
    List<AcRecord> listByAuditorId(@Param("uid") int uid);

    // todo 修改
    // 申请人获取自己的AC日志
    @Query("select a from AcRecord a where a.user.id = :uid")
    List<AcRecord> listByApplicantId(@Param("uid") int uid);


    // 获取所有用户的AC日志
    @Query(value = "SELECT a.reason, a.ac, a.create_time, u.name as auditor, a.classify FROM ac_record a LEFT JOIN user u on a.auditor_id = u.id where user_id = :uid  order by a.create_time desc", nativeQuery = true)
    List<Map<String, Object>> listUserAc(@Param("uid") int uid);


    // 获取指定用户的总ac
    @Query(value = "select ifnull((select sum(ac) from ac_record where user_id = :uid), 0)", nativeQuery = true)
    Double getUserAcSum(@Param("uid") int uid);


    // 所有用户的总AC
    @Query(value = "select u.id, u.name, ifnull(sum(a.ac), 0)  as total from user u left join ac_record a on u.id = a.user_id group by u.id order by total DESC", nativeQuery = true)
    List<Map<String, Object>> listAcSummary();


    // 获取用户指定日期前的 AC和，用于计算绩效
    @Query(value = "select ifnull((select sum(ac) from ac_record where user_id = :uid and DATE_FORMAT(`create_time`,'%Y%m') <= :yearmonth), 0)", nativeQuery = true)
    Double getUserAcByDate(@Param("uid") int uid, @Param("yearmonth") int yearmonth);




}
