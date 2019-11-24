package com.softeng.dingtalk;

import com.softeng.dingtalk.component.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.Calendar;

/**
 * @author zhanyeye
 * @description
 * @create 12/29/2019 10:49 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestIdea {
    @Test
    public void test() {
        Calendar calendar = Calendar.getInstance();
        calendar.setMinimalDaysInFirstWeek(4);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        log.debug(calendar.toString());

        calendar.set(2019, Calendar.OCTOBER, 31);
        log.debug(calendar.get(Calendar.WEEK_OF_MONTH) + "==? 5   2019.10.31-4");

        calendar.set(2019, Calendar.NOVEMBER, 1);
        log.debug(calendar.get(Calendar.WEEK_OF_MONTH) + "==? 0   2019.11.01-5");

        calendar.set(2019, Calendar.NOVEMBER, 4);
        log.debug(calendar.get(Calendar.WEEK_OF_MONTH) + "==? 1   2019.11.04-1");

        calendar.set(2019, Calendar.NOVEMBER, 24);
        log.debug(calendar.get(Calendar.WEEK_OF_MONTH) + "==? 3   2019.11.24-7");

        calendar.set(2019, Calendar.NOVEMBER, 30);
        log.debug(calendar.get(Calendar.WEEK_OF_MONTH) + "==? 4   2019.11.30-6");

        calendar.set(2019, Calendar.DECEMBER, 1);
        log.debug(calendar.get(Calendar.WEEK_OF_MONTH) + "==? 0   2019.12.01-7");

        calendar.set(2019, Calendar.DECEMBER, 2);
        log.debug(calendar.get(Calendar.WEEK_OF_MONTH) + "==? 1   2019.12.02-1");

        calendar.set(2019, Calendar.DECEMBER, 31);
        log.debug(calendar.get(Calendar.WEEK_OF_MONTH) + "==? 5   2019.12.31-2");

        calendar.set(2020, Calendar.JANUARY, 1);
        log.debug(calendar.get(Calendar.WEEK_OF_MONTH) + "==? 1   2020.1.1-3");

    }


    @Autowired
    Utils utils;
    @Test
    public void test_date() {
//        int day = LocalDate.now().getDayOfMonth();
//        log.debug("{}",day);
//        int month = LocalDate.now().getMonthValue();
//        log.debug("{}", month);
//        int week = LocalDate.now().getDayOfWeek().getValue();
//        log.debug("{}",week);
        log.debug(getTimeFlag(LocalDate.of(2019, 10, 31)) + "");
        log.debug(getTimeFlag(LocalDate.of(2019, 11, 1)) + "");
        log.debug(getTimeFlag(LocalDate.of(2019, 11, 4)) + "");
        log.debug(getTimeFlag(LocalDate.of(2019, 11, 24)) + "");
        log.debug(getTimeFlag(LocalDate.of(2019, 11, 30)) + "");
        log.debug(getTimeFlag(LocalDate.of(2019, 12, 1)) + "");
        log.debug(getTimeFlag(LocalDate.of(2019, 12, 2)) + "");
        log.debug(getTimeFlag(LocalDate.of(2019, 12, 31)) + "");
        log.debug(getTimeFlag(LocalDate.of(2017, 1, 1)) + "");

    }


    public int getTimeFlag(LocalDate localDate) {
        int week;
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        int beginDayOfWeek = LocalDate.of(year, month, 1).getDayOfWeek().getValue();

        log.debug("" + year + "-" + month + "-" + day + "-" + beginDayOfWeek);

        if (beginDayOfWeek <= 4) { //所跨周算本月第一周
            if(day > (8 - beginDayOfWeek)) {
                week = 1 + (int)Math.ceil((day - (8 - beginDayOfWeek)) / 7.0);
            } else { //本月第1周
                week = 1;
            }
        } else {  //所跨周算上个月最后一周
            if (day > (8 - beginDayOfWeek)) {
                week = (int)Math.ceil((day - (8 - beginDayOfWeek)) / 7.0);
            } else { //上月最后一周
                if (month == 1) {
                    month = 12;
                    year--;
                } else {
                    month--;
                }
                LocalDate lastMonth = localDate.minusDays(day);
                int lastMonthDays = lastMonth.getDayOfMonth();
                int lastMonthEndWeek = lastMonth.getDayOfWeek().getValue();
                int lastMonthBeginWeek = lastMonth.minusDays(lastMonthDays - 1).getDayOfWeek().getValue();
                if (lastMonthEndWeek >= 4 && lastMonthBeginWeek <= 4) {
                    week = 5;
                } else {
                    week = 4;
                }
            }
        }

        return year * 1000 + month * 10 + week;
    }

}
