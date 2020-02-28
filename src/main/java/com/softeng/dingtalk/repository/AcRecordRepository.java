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



    @Query(value = "SELECT a.reason, a.ac, a.create_time, u.name as auditor, a.classify FROM ac_record a LEFT JOIN user u on a.auditor_id = u.id where user_id = :uid  order by a.create_time desc", nativeQuery = true)
    List<Map<String, Object>> listUserAc(@Param("uid") int uid);

    /**
     * 获取指定用户的总ac
     * @param uid
     * @return java.lang.Double
     * @Date 10:03 PM 1/10/2020
     **/
    @Query(value = "select ifnull((select sum(ac) from ac_record where user_id = :uid), 0)", nativeQuery = true)
    Double getUserAcSum(@Param("uid") int uid);

    //"select  new com.softeng.dingtalk.vo.AcVO(a.user.id, a.user.name, sum(a.ac)) from User u left join AcRecord a  on u.id = a.user.id group by a.user.id"
    @Query(value = "select u.id, u.name, ifnull(sum(a.ac), 0)  as total from User u left join ac_record a on u.id = a.user_id group by u.id order by total DESC", nativeQuery = true)
    List<Map<String, Object>> listAcSummary();

    @Query(value = "select ifnull((select sum(ac) from ac_record where user_id = :uid and DATE_FORMAT(`create_time`,'%Y%m') <= :yearmonth), 0)", nativeQuery = true)
    Double getUserAcByDate(@Param("uid") int uid, @Param("yearmonth") int yearmonth);




}
