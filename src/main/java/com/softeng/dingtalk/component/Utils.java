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

    /**
     * 计算当前日期是本月的第几周 ISO 8601
     * @param localDate
     * @return int[]
     * @Date 4:16 PM 2/3/2020
     **/
    public int[] getTimeFlag(LocalDate localDate) {
        int week;
        int year = localDate.getYear();        //年
        int month = localDate.getMonthValue(); //月
        int day = localDate.getDayOfMonth();   //日
        int firstDayWeek = LocalDate.of(year, month, 1).getDayOfWeek().getValue(); //当前月第1天是周几


        // (7 - firstDayWeek) + 1 :本月第一天到本月第一个周日，共有多少天
        // day - [(7 - firstDayWeek) + 1] 除去跨月周的第一天到当前天，有多少天

        if (firstDayWeek == 1) { // 本月1日正好是周一，不存在跨月周
            week = (int)Math.ceil(day / 7.0);
            if (week == 5) {
                week = 1;
                if (month == 12) {
                    month = 1;
                    year++;
                } else {
                    month++;
                }
            }
        } else if (firstDayWeek <= 4) { // 所跨周算本月第一周
            if(day > (8 - firstDayWeek)) { // 天数 > (7 - firstDayWeek) + 1 : 当前天超过了第1周的范围,
                week = 1 + (int)Math.ceil((day - (8 - firstDayWeek)) / 7.0);
                if (week == 5) {
                    int endDayWeek = LocalDate.of(year, month, localDate.lengthOfMonth()).getDayOfWeek().getValue();
                    if (endDayWeek < 4) {
                        week = 1;
                        if (month == 12) {
                            month = 1;
                            year++;
                        } else {
                            month++;
                        }
                    }
                }
            } else {
                week = 1; // 本月第1周
            }
        } else {  // 所跨周算上个月最后一周
            if (day > (8 - firstDayWeek)) { // 当前天不算上个月最后一周
                week = (int)Math.ceil((day - (8 - firstDayWeek)) / 7.0);
                if (week == 5) {
                    week = 1;
                    if (month == 12) {
                        month = 1;
                        year++;
                    } else {
                        month++;
                    }
                }
            } else { //当前天算上月最后一周
                if (month == 1) {
                    month = 12;
                    year--;
                } else {
                    month--;
                }
                LocalDate lastMonth = localDate.minusDays(day); // 上个月
                int lastMonthDays = lastMonth.getDayOfMonth();  // 上个月天数
                int lastMonthEndWeek = lastMonth.getDayOfWeek().getValue(); // 上个月最后一天是第几周
                int lastMonthBeginWeek = lastMonth.minusDays(lastMonthDays - 1).getDayOfWeek().getValue(); // 上个月第一天是第几周
                if (lastMonthEndWeek >= 4 && lastMonthBeginWeek <= 4) {
                    week = 5;
                } else {
                    week = 4;
                }
            }
        }
        log.debug(year + " - " + month + " - " + week);
        int[] result = new int[2];
        result[0] = year * 100 + month;
        result[1] = week;
        return result;
    }
}
