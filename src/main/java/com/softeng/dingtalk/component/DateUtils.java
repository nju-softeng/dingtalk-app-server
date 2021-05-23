package com.softeng.dingtalk.component;

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
public class DateUtils {

    /**
     * 将日期转化为 YYYYMMW 的形式 <p>
     * 如 2020年1月的第1周 -> 2020011
     * @param date
     * @return
     */
    public int getDateCode(LocalDate date) {
        // 获取 date 所在周的周日
        LocalDate sunday = date.plusDays(7 - date.getDayOfWeek().getValue());
        // 所在年份
        int year = sunday.getYear();
        // 所在月份
        int month = sunday.getMonthValue();
        // 当月第几天
        int day = sunday.getDayOfMonth();
        // 计算 date 属于本月第几周
        int week = day / 7 + (day % 7 == 0 ? 0 : 1);
        return year * 1000 + month * 10 + week;
    }

    /**
     * 返回当前日期对应的周数，用于前端显示
     * @param date
     * @return
     */
    public String getDateStr(LocalDate date) {
        // 获取 date 所在周的周日
        LocalDate sunday = date.plusDays(7 - date.getDayOfWeek().getValue());
        int month = sunday.getMonthValue();
        int day = sunday.getDayOfMonth();
        int week = day / 7 + (day % 7 == 0 ? 0 : 1);
        return month + " 月 第 " + week + " 周";
    }

}
