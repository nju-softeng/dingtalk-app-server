package com.softeng.dingtalk;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.LocalDateTime;


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
@SpringBootTest
@Slf4j
public class TestIdea {

    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    Utils utils;
    @Autowired
    DingTalkUtils dingTalkUtils;
    @Autowired
    UserService userService;

//    @Test
//    public void test_sync() throws ExecutionException, InterruptedException {
//        long start = System.currentTimeMillis();
//        List<Future<Map>> futures = new ArrayList<>();
//        for (int i = 0; i <150; i++) {
//
//            String userid = userService.getUserid(1);
//            futures.add(dingTalkUtils.getReport(userid, LocalDateTime.of(2020, 1, 19, 0, 0)));
//        }
//        List<Object> map = new ArrayList<>();
//        for (Future future : futures) {
//            map.add(future.get());
//        }
//
//        log.debug(String.valueOf(System.currentTimeMillis() - start));
//        log.debug(map.toString());
//    }



}
