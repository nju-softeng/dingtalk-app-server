package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.entity.DcSummary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhanyeye
 * @description
 * @date 12/29/2019
 */
@Repository
public interface DcSummaryRepository extends CustomizedRepository<DcSummary, Integer> {

    /**
     * 查询指定月份的 dc_summary
     * @param yearmonth
     * @return
     */
    List<DcSummary> findAllByYearmonth(int yearmonth);

    /**
     * 获取用户指定月份的 dcSummary
     * @param uid
     * @param yearmonth
     * @return
     */
    @Query("select  d from DcSummary d where d.user.id = :uid and d.yearmonth = :yearmonth")
    DcSummary getDcSummary(@Param("uid") int uid, @Param("yearmonth") int yearmonth);

    /**
     * 获取指定月份所有用户的绩效汇总(降序)
     * @param yearmonth
     * @return
     */
    @Query(value = "SELECT u.id AS uid, u.stu_num,  u.name, d.salary," +
            " d.week1, d.week2, d.week3, d.week4, d.week5, d.ac, d.total as total, d.topup " +
            "FROM (SELECT id, name, stu_num FROM `user` WHERE position != '待定' and is_deleted = 0) u LEFT JOIN dc_summary d ON u.id = d.user_id AND" +
            " d.yearmonth = :yearmonth ORDER BY salary DESC",nativeQuery = true)
    List<Map<String, Object>> listDcSummaryDesc(int yearmonth);

    /**
     * 获取指定月份所有用户的绩效汇总（升序）
     * @param yearmonth
     * @return
     */
    @Query(value = "SELECT u.id AS uid, u.stu_num,  u.name, d.salary," +
            " d.week1, d.week2, d.week3, d.week4, d.week5, d.ac, d.total as total, d.topup " +
            "FROM (SELECT id, name, stu_num FROM `user` WHERE position != '待定' and is_deleted = 0) u LEFT JOIN dc_summary d ON u.id = d.user_id AND" +
            " d.yearmonth = :yearmonth ORDER BY salary ASC",nativeQuery = true)
    List<Map<String, Object>> listDcSummaryAsc(int yearmonth);

    /**
     * 获取指定月份的 总dc
     * @param uid
     * @param yearmonth
     * @return
     */
    @Query(value = "select ifnull((select total from dc_summary where user_id = :uid and yearmonth = :yearmonth), 0)", nativeQuery = true)
    Double getDcTotal(@Param("uid") int uid, @Param("yearmonth") int yearmonth);

    /**
     * 获取dc排名
     * @param uid
     * @param yearmonth
     * @return
     */
    @Query(value = "SELECT count(id) + 1 FROM dc_summary WHERE yearmonth = :yearmonth AND total > (SELECT IFNULL((SELECT total FROM dc_summary WHERE user_id = :uid and yearmonth = :yearmonth),0))", nativeQuery = true)
    int getRank(@Param("uid") int uid, @Param("yearmonth") int yearmonth);

    /**
     * 更新助研金
     * @param uid
     * @param ac
     * @param topup
     * @param salary
     */
    @Modifying
    @Query(value = "update DcSummary d set d.ac = :ac, d.topup = :topup, d.salary = :salary where d.user.id = :uid and d.yearmonth = :yearmonth")
    void updateSalary(int uid, int yearmonth, double ac, double topup, double salary);

    /**
     * 查询用户制定月的各周dc
     * @param uid
     * @param yearmonth
     * @return
     */
    @Query(value = "select dc from DcSummary dc where dc.user.id = :uid and dc.yearmonth = :yearmonth")
    DcSummary findByUserIdAndYearmonth(int uid, int yearmonth);

    /**
     * 查询用户指定月份的 topup
     * @param uid
     * @param yearmonth
     * @return
     */
    @Query(value = "select ifnull((select topup from dc_summary where user_id = :uid and yearmonth = :yearmonth), 0)", nativeQuery = true)
    Double findTopup(int uid, int yearmonth);

    /**
     * 查询指定月份，已经存在的 dc_summary 的用户id
     * @param yearmonth
     * @return
     */
    @Query("select dc.user.id from DcSummary dc where dc.yearmonth = :yearmonth")
    Set<Integer> findIdsByDate(int yearmonth);



}
