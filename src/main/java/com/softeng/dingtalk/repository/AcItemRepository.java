package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 12/11/2019
 */
@Repository
public interface AcItemRepository extends CustomizedRepository<AcItem, Integer>, JpaRepository<AcItem, Integer> {
    @Query("select a from AcItem a where a.application.id = :id")
    List<AcItem> findAllByApplicationID(@Param("id") int id);
}
