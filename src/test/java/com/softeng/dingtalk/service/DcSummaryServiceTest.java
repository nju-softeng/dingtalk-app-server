package com.softeng.dingtalk.service;

import com.softeng.dingtalk.repository.DcSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanyeye
 * @description
 * @create 1/29/2020 2:36 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DcSummaryServiceTest {
    @Autowired
    DcSummaryRepository dcSummaryRepository;

    @Test
    public void test() {
        dcSummaryRepository.listDcSummary(202001);
    }

}
