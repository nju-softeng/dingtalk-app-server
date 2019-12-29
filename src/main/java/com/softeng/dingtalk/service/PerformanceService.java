package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.repository.DcSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description 绩效相关业务逻辑
 * @create 12/29/2019 9:57 AM
 */
@Service
@Transactional
public class PerformanceService {
    @Autowired
    DcSummaryRepository dcSummaryRepository;
    @Autowired
    DcRecordRepository dcRecordRepository;

    public void updateWeekTotalDc(int uid, LocalDateTime localDateTime, int week) {
        Integer totalDc = dcRecordRepository.getUserWeekTotalDc(uid, localDateTime.toString().substring(0, 7).replace("-", ""), week);
        if (totalDc != null) {
            
        }


    }

}
