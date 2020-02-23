package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.DcSummary;
import com.softeng.dingtalk.vo.DcSummaryVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @date 12/29/2019
 */
@Repository
public interface DcSummaryRepository extends JpaRepository<DcSummary, Integer> {

    /**
     * 获取指定用户指定日期的dc汇总值
     * @param uid, yearmonth, week
     * @return java.util.List<java.lang.Integer>
     * @Date 10:58 AM 12/30/2019
     **/
    @Query("select  d from DcSummary d where d.user.id = :uid and d.yearmonth = :yearmonth")
    DcSummary getDcSummary(@Param("uid") int uid, @Param("yearmonth") int yearmonth);

//    @Query("select new com.softeng.dingtalk.vo.DcSummaryVO(u.name, d.yearmonth, d.week1, d.week2, d.week3, d.week4, d.week5, d.total) " +
////            "from User u left join DcSummary d on u.id = d.user.id  where d.yearmonth = :yearmonth")
    // SELECT u.`name`, d.week1  FROM  `user` u left  JOIN  (SELECT * FROM dc_summary WHERE yearmonth = 202002) d on u.id = d.user_id
    @Query(value = "select u.name, d.yearmonth, d.week1, d.week2, d.week3, d.week4, d.week5, d.total from user u left join (select * from dc_summary where yearmonth = :yearmonth) d on u.id = d.user_id",nativeQuery = true)

    List<Map<String, Object>> listDcSummary(@Param("yearmonth") int yearmonth);


}
