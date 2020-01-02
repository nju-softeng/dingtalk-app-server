package com.softeng.dingtalk.service;

import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.repository.DcSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author zhanyeye
 * @description
 * @create 1/2/2020 7:58 PM
 */

@Service
@Transactional
@Slf4j
public class DcSummaryService {
    @Autowired
    DcSummaryRepository dcSummaryRepository;
    @Autowired
    DcRecordRepository dcRecordRepository;

    public void updateDcSummary(int dcid) {


    }

}
