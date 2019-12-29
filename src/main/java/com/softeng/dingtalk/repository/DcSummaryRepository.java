package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.DcSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhanyeye
 * @description
 * @date 12/29/2019
 */
@Repository
public interface DcSummaryRepository extends CustomizedRepository<DcSummary, Integer>, JpaRepository<DcSummary, Integer> {
}
