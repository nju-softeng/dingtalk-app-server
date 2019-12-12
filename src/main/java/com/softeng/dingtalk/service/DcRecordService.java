package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.repository.DcRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhanyeye
 * @description
 * @create 12/12/2019 10:48 AM
 */
public class DcRecordService {
    @Autowired
    DcRecordRepository dcRecordRepository;

    public void addDcRecord(DcRecord dcRecord) {
        dcRecordRepository.save(dcRecord);
    }
}
