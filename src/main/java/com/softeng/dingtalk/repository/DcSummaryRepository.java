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

    // 获取用户指定月份的 dcSummary
    @Query("select  d from DcSummary d where d.user.id = :uid and d.yearmonth = :yearmonth")
    DcSummary getDcSummary(@Param("uid") int uid, @Param("yearmonth") int yearmonth);


    // 获取指定月份所有用户的绩效汇总
    @Query(value = "SELECT u.id, d.yearmonth,  u.name, d.salary, d.week1, d.week2, d.week3, d.week4, d.week5,d.ac, d.total as total,d.topup FROM user u LEFT JOIN dc_summary d ON u.id = d.user_id AND d.yearmonth = :yearmonth ORDER BY total DESC",nativeQuery = true)
    List<Map<String, Object>> listDcSummary(@Param("yearmonth") int yearmonth);


    // 获取指定月份的 总dc
    @Query(value = "select ifnull((select total from dc_summary where user_id = :uid and yearmonth = :yearmonth), 0)", nativeQuery = true)
    Double getDcTotal(@Param("uid") int uid, @Param("yearmonth") int yearmonth);


    // 获取dc排名
    @Query(value = "SELECT count(id) + 1 FROM dc_summary WHERE yearmonth = :yearmonth AND total > (SELECT IFNULL((SELECT total FROM dc_summary WHERE user_id = :uid and yearmonth = :yearmonth),0))", nativeQuery = true)
    int getRank(@Param("uid") int uid, @Param("yearmonth") int yearmonth);


    // 更新助研金
    @Modifying
    @Query(value = "update DcSummary d set d.ac = :ac, d.topup = :topup, d.salary = :salary where d.user.id = :uid")
    void updateSalary(@Param("uid") int uid,@Param("ac") double ac, @Param("topup") double topup, @Param("salary") double salary);


    DcSummary findByUserIdAndYearmonth(int uid, int yearmonth);

}
