package com.softeng.dingtalk.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    DingTalkUtils dingTalkUtils;

    @Test
    public void test() throws Exception {

    }


}
