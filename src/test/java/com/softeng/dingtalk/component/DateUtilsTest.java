package com.softeng.dingtalk.component;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * @Author zhanyeye
 * @Description
 * @Date 19/10/2021
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DateUtilsTest {
    @Autowired
    DateUtils dateUtils;

    @Test
    public void testGetWeekBeginAndStart() {
        Assert.assertArrayEquals(dateUtils.getWeekBeginAndStart(YearMonth.of(2021, 9), 1), new LocalDate[]{LocalDate.of(2021, 9, 1), LocalDate.of(2021, 9, 5)});
        Assert.assertArrayEquals(dateUtils.getWeekBeginAndStart(YearMonth.of(2021, 9), 2), new LocalDate[]{LocalDate.of(2021, 9, 6), LocalDate.of(2021, 9, 12)});
        Assert.assertArrayEquals(dateUtils.getWeekBeginAndStart(YearMonth.of(2021, 9), 3), new LocalDate[]{LocalDate.of(2021, 9, 13), LocalDate.of(2021, 9, 19)});
        Assert.assertArrayEquals(dateUtils.getWeekBeginAndStart(YearMonth.of(2021, 9), 4), new LocalDate[]{LocalDate.of(2021, 9, 20), LocalDate.of(2021, 9, 26)});
        Assert.assertArrayEquals(dateUtils.getWeekBeginAndStart(YearMonth.of(2021, 9), 5), null);
    }
}
