package com.softeng.dingtalk.service;

import com.softeng.dingtalk.excel.DcSummaryData;
import com.softeng.dingtalk.dao.mapper.DcSummaryMapper;
import com.softeng.dingtalk.dao.repository.DcSummaryRepository;
import com.softeng.dingtalk.dao.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 1/29/2020 2:36 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class PerformanceServiceTest {
    @Autowired
    DcSummaryRepository dcSummaryRepository;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SystemService systemService;
    @Autowired
    DcSummaryMapper dcSummaryMapper;

    @Test
    public void test1() {
        List<DcSummaryData> dataList = dcSummaryMapper.listDcSummaryDataByYearMonth(202110);
        log.info(dataList.toString());

    }



}
