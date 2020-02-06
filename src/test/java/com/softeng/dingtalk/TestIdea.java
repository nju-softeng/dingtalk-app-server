package com.softeng.dingtalk;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.repository.AcItemRepository;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;


import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * @author zhanyeye
 * @description
 * @create 12/29/2019 10:49 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Transactional
public class TestIdea {

    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    Utils utils;
    @Autowired
    DingTalkUtils dingTalkUtils;
    @Autowired
    UserService userService;
    @Autowired
    AcItemRepository acItemRepository;

    @Test
    public void test_sync() throws ExecutionException, InterruptedException {
        log.debug(LocalTime.now()+ "");
    }


    @Test
    public void test() {
        log.debug(("231".isEmpty()) + ""  );
    }


}
