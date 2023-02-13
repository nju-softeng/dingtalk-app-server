package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.PrizePo;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Description
 * @Author Jerrian Zhao
 * @Data 01/25/2022
 */

public interface PrizeRepository extends CustomizedRepository<PrizePo, Integer> {

    /**
     * 查询用户所有奖项
     *
     * @param userId
     * @return
     */
    @Query(value = "select * from prize pr where user_id=:userId and deleted = false ", nativeQuery = true)
    List<PrizePo> findByUserId(int userId);
}
