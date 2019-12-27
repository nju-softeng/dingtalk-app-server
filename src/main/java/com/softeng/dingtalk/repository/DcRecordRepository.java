package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.DcRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhanyeye
 * @description
 * @date 12/12/2019
 */
public interface DcRecordRepository extends CustomizedRepository<DcRecord, Integer>, JpaRepository<DcRecord, Integer> {

}
