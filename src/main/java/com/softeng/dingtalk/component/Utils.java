package com.softeng.dingtalk.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.Calendar;
import java.util.Map;

/**
 * @author zhanyeye
 * @description 工具类
 * @create 12/31/2019 3:54 PM
 */
@Slf4j
@Component
public class Utils {

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

    public int getTimeCode(LocalDate localDate) {
        LocalDate sunday = localDate.plusDays(7 - localDate.getDayOfWeek().getValue()); // 获取本月周日
        int year = sunday.getYear();        //年
        int month = sunday.getMonthValue(); //月
        int week = sunday.getDayOfMonth() / 7 + 1;
        return year * 1000 + month * 10 + week;
    }

    public String getTimeStr(LocalDate localDate) {
        LocalDate sunday = localDate.plusDays(7 - localDate.getDayOfWeek().getValue()); // 获取本月周日
        int month = sunday.getMonthValue(); //月
        int week = sunday.getDayOfMonth() / 7 + 1;
        return month + " 月 第 " + week + " 周";
    }

}
