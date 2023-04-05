package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.Property;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Description
 * @Author Jerrian Zhao
 * @Data 01/26/2022
 */

public interface PropertyRepository extends CustomizedRepository<Property, Integer> {

    /**
     * 查询用户所有固定资产
     *
     * @param userId
     * @return
     */
    @Query(value = "select * from property where user_id = :userId and deleted=false", nativeQuery = true)
    List<Property> findByUserId(int userId);
}
