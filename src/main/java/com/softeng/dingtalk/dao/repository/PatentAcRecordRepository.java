package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.entity.PatentAcRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatentAcRecordRepository extends CustomizedRepository<PatentAcRecord, Integer> {

    List<PatentAcRecord> findAllByPatentId(int patentId);
}
