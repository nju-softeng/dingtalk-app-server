package com.softeng.dingtalk.service;

import com.softeng.dingtalk.repository.DcSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

//    public updateDcSummary(int year, int month, int week, int ) {
//
//    }


}
