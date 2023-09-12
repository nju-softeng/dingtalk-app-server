package com.softeng.dingtalk.dao.repository;


import com.softeng.dingtalk.entity.SubsidyLevel;
import com.softeng.dingtalk.enums.Position;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author zhanyeye
 * @description
 * @date 5/28/2020
 */
public interface SubsidyLevelRepository extends CustomizedRepository<SubsidyLevel, Integer> {
    /**
     * 查询绩效标准
     * @param position
     * @return double
     */
    @Query("select s.subsidy from SubsidyLevel s where s.position = :position")
    double getSubsidy(@Param("position") Position position);


    /**
     * 更新绩效标准
     * @param position
     * @param subsidy
     */
    @Modifying
    @Query("update SubsidyLevel set subsidy = :subsidy where position = :position")
    void updateSubsidy(@Param("position") Position position, @Param("subsidy") double subsidy);
}
