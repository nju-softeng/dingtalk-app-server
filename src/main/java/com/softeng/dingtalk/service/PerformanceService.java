package com.softeng.dingtalk.service;

import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.DcSummaryRepository;
import com.softeng.dingtalk.vo.DcSummaryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 9:48 AM
 */
@Service
@Transactional
@Slf4j
public class PerformanceService {
    @Autowired
    DcSummaryRepository dcSummaryRepository;
    @Autowired
    AcRecordRepository acRecordRepository;

    public List<Map<String, Object>> listDcSummaryVO(LocalDate date) {
        int yearmonth = date.getYear() * 100 + date.getMonthValue();
        log.debug(yearmonth + "");
        return dcSummaryRepository.listDcSummary(yearmonth);
    }


    public List<Object> listAcSummary() {
        return acRecordRepository.listAcSummary();
    }

}
