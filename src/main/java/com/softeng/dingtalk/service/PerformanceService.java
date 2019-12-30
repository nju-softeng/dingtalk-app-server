package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.entity.DcSummary;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.repository.DcSummaryRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PerformanceService {
    @Autowired
    DcSummaryRepository dcSummaryRepository;
    @Autowired
    DcRecordRepository dcRecordRepository;

    public void updateWeekTotalDc(int uid, LocalDateTime localDateTime, int week) {
        log.debug(localDateTime.toString());
        log.debug(week + "");
        log.debug("user:" + uid);
        String yearmonth = localDateTime.toString().substring(0, 7);
        Double totalDc = dcRecordRepository.getUserWeekTotalDc(uid, yearmonth, week);
        if (totalDc != null) {
            //todo 如果有 dcsummary 数据更新值，否则新建
            DcSummary dcSummary = dcSummaryRepository.getDcSummaryID(uid, yearmonth, week);
            if (dcSummary != null) {
                dcSummary.setDc(totalDc);
                dcSummaryRepository.save(dcSummary);
            } else {
                dcSummaryRepository.save(new DcSummary(localDateTime.getYear(), localDateTime.getMonthValue(),week, totalDc, new User(uid)));
            }
        }
    }

}
