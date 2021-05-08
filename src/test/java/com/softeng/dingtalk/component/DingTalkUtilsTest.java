package com.softeng.dingtalk.component;

import com.softeng.dingtalk.api.MessageApi;
import com.softeng.dingtalk.api.ReportApi;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 12/12/2019 3:34 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DingTalkUtilsTest {

    @Autowired
    MessageApi messageApi;
    @Autowired
    ReportApi reportApi;

    @Test
    public void testGetReport() {
        LocalDateTime startTime = LocalDateTime.of(2021, 4, 1, 8, 0);
        Map map = reportApi.getReport("306147243334957616", startTime, startTime.plusDays(5));
        log.debug(map.get("contents").toString());
    }



//    @Test
//    public void testBackLog() throws Exception {
//        messageApi.sendWorkMessage();
//    }
}
