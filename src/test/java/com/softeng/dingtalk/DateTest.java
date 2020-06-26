package com.softeng.dingtalk;

import com.softeng.dingtalk.mapper.PaperMapper;
import com.softeng.dingtalk.vo.PaperInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 2/27/2020 1:42 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DateTest {

    @Autowired
    PaperMapper paperMapper;

    @Test
    public void test_1() {
        List<PaperInfoVO> kk = paperMapper.listPaperInfo();
    }


}
