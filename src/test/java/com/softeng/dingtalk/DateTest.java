package com.softeng.dingtalk;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

/**
 * @author zhanyeye
 * @description
 * @create 2/27/2020 1:42 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DateTest {


    @Test
    public void test_1() {
        int[] arr = getTimeFlag(LocalDate.of(2020, 1, 20));
        log.debug(arr[0]+ " " + arr[1]);
    }

    public int[] getTimeFlag(LocalDate localDate) {
        LocalDate sunday = localDate.plusDays(7 - localDate.getDayOfWeek().getValue()); // 获取本月周日
        int year = sunday.getYear();        //年
        int month = sunday.getMonthValue(); //月
        int week = sunday.getDayOfMonth() / 7 + 1;

        int[] result = new int[2];
        result[0] = year * 100 + month;
        result[1] = week;
        return result;
    }

}
