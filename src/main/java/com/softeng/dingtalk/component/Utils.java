package com.softeng.dingtalk.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.Calendar;

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
     * @return java.lang.String
     * @Date 3:59 PM 12/31/2019
     **/
    public int getTimeFlag() {
        int week;
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        int day = LocalDate.now().getDayOfMonth();
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
                LocalDate lastMonth = LocalDate.now().minusDays(day);
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
