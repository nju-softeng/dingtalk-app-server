package com.softeng.dingtalk;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.component.Timer;
import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.enums.Position;
import com.softeng.dingtalk.mapper.DcRecordMapper;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.service.*;
import com.softeng.dingtalk.vo.DcRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author zhanyeye
 * @description
 * @create 12/29/2019 10:49 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class TestIdea {

    @Autowired
    SubsidyLevelRepository subsidyLevelRepository;
    @Autowired
    SystemService systemService;
    @Autowired
    DcRecordMapper dcRecordMapper;

    @Test
    public void test() {
        List<DcRecordVO> res = dcRecordMapper.listDcRecordVO(1, 1, 1);
    }



}
