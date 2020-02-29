package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.DcSummary;
import com.softeng.dingtalk.vo.DcSummaryVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    @Query(value = "SELECT u.id, d.yearmonth,  u.name, d.salary, d.week1, d.week2, d.week3, d.week4, d.week5,d.ac, d.total as total,d.topup FROM user u LEFT JOIN dc_summary d ON u.id = d.user_id AND d.yearmonth = :yearmonth ORDER BY total DESC",nativeQuery = true)
    List<Map<String, Object>> listDcSummary(@Param("yearmonth") int yearmonth);


    @Query(value = "select ifnull((select total from dc_summary where user_id = :uid and yearmonth = :yearmonth), 0)", nativeQuery = true)
    Double getDcTotal(@Param("uid") int uid, @Param("yearmonth") int yearmonth);

    @Modifying
    @Query(value = "update DcSummary d set d.ac = :ac, d.topup = :topup, d.salary = :salary where d.user.id = :uid")
    void updateSalary(@Param("uid") int uid,@Param("ac") double ac, @Param("topup") double topup, @Param("salary") double salary);

}
