package com.softeng.pms.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author zhanyeye
 * @description 工具类
 * @create 12/31/2019 3:54 PM
 */
@Slf4j
@Component
public class Utils {

    /**
     * 将日期转化为2个值: 年月，周 (YYYYMM, week),用数组返回      <p>
     * 如 2020年1月第1周 -> [202001, 1]
     * @param localDate
     * @return
     */
    public int[] getTimeFlag(LocalDate localDate) {
        // 获取本月周日
        LocalDate sunday = localDate.plusDays(7 - localDate.getDayOfWeek().getValue());
        // 年
        int year = sunday.getYear();
        // 月
        int month = sunday.getMonthValue();
        // 天
        int day = sunday.getDayOfMonth();
        int week = day / 7 + (day % 7 == 0 ? 0 : 1);

        int[] result = new int[2];
        result[0] = year * 100 + month;
        result[1] = week;
        return result;
    }


    /**
     * 将日期转化为 YYYYMMW 的形式 <p>
     * 如 2020年1月第1周 -> 2020011
     * @param localDate
     * @return
     */
    public int getTimeCode(LocalDate localDate) {
        // 获取本月周日
        LocalDate sunday = localDate.plusDays(7 - localDate.getDayOfWeek().getValue());
        // 年
        int year = sunday.getYear();
        // 月
        int month = sunday.getMonthValue();
        int day = sunday.getDayOfMonth();
        int week = day / 7 + (day % 7 == 0 ? 0 : 1);
        return year * 1000 + month * 10 + week;
    }


    public String getTimeStr(LocalDate localDate) {
        // 获取本月周日
        LocalDate sunday = localDate.plusDays(7 - localDate.getDayOfWeek().getValue());
        int month = sunday.getMonthValue();
        int day = sunday.getDayOfMonth();
        int week = day / 7 + (day % 7 == 0 ? 0 : 1);
        return month + " 月 第 " + week + " 周";
    }

}
