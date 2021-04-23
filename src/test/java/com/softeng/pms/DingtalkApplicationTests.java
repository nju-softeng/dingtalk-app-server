package com.softeng.pms;

import com.softeng.pms.component.DingTalkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DingtalkApplicationTests {

    @Autowired
    DingTalkUtils dingTalkUtils;



}
