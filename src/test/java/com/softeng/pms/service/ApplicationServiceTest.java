package com.softeng.pms.service;

import com.softeng.pms.repository.DcRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanyeye
 * @description
 * @create 12/12/2019 8:21 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ApplicationServiceTest {
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    ApplicationService applicationService;

    @Test
    public void test() {
        log.debug(dcRecordRepository.isExist(1, 1, 202007, 2).toString());
    }

}
