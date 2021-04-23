package com.softeng.pms;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.softeng.pms.mapper.PaperMapper;
import com.softeng.pms.vo.PaperInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        PageHelper.startPage(1, 2);
        List<PaperInfoVO> kk = paperMapper.listPaperInfo(0, 6);
        PageInfo<PaperInfoVO> pages = new PageInfo<>(kk);
        log.debug(pages.toString());


    }


}
