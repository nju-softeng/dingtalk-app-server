package com.softeng.dingtalk.service;

import com.softeng.dingtalk.repository.DcRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 12/12/2019 8:21 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ApplicationServiceTest {
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    ApplicationService applicationService;

    @Test
    public void test() {
        log.debug(applicationService.isExist(5, 1, 1) + "");
    }

}
