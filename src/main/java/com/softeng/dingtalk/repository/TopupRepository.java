package com.softeng.dingtalk.repository;


import com.softeng.dingtalk.entity.Topup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author zhanyeye
 * @description
 * @date 2/27/2020
 */
public interface TopupRepository extends JpaRepository<Topup, Integer> {
    @Query(value = "select ifnull((select sum(subsidy) from topup where user_id = :uid), 0)", nativeQuery = true)
    Double getByUserid(@Param("uid") int uid);
}
