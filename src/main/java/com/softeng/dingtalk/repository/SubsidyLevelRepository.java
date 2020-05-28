package com.softeng.dingtalk.repository;


import com.softeng.dingtalk.entity.SubsidyLevel;
import com.softeng.dingtalk.enums.Position;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author zhanyeye
 * @description
 * @date 5/28/2020
 */
public interface SubsidyLevelRepository extends CustomizedRepository<SubsidyLevel, Integer> {
    @Query("select s.subsidy from SubsidyLevel s where s.position = :position")
    double getSubsidy(@Param("position") Position position);
}
