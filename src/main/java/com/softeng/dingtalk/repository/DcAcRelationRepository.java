package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.DcAcRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 1/30/2020
 */
public interface DcAcRelationRepository extends JpaRepository<DcAcRelation, Integer> {
    @Query("select d.acRecordId from DcAcRelation d where d.dcRecordId = :dcRecordId")
    List<Integer> listAcId(@Param("dcRecordId") int dcRecordId);
}
