package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.AcItem;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 申请人获取已经提交的申请
 * @create 2/3/2020 11:12 PM
 */
@Getter
@Setter
public class AppliedVO {
    private int id;
    private int auditorid;
    private String auditorName;
    private boolean status;
    private double dvalue;
    private double dc;
    private double ac;
    private int yearmonth;
    private int week;
    private LocalDate date;
    private LocalDateTime insertTime;
    private List<AcItem> acItems;     //ac值申请列表

    public AppliedVO(int id, int auditorid, String auditorName, boolean status, double dvalue, double dc, double ac, int yearmonth, int week, LocalDate date, LocalDateTime insertTime) {
        this.id = id;
        this.auditorid = auditorid;
        this.auditorName = auditorName;
        this.status = status;
        this.dvalue = dvalue;
        this.dc = dc;
        this.ac = ac;
        this.date = date;
        this.insertTime = insertTime;
        this.yearmonth = yearmonth;
        this.week = week;

    }
}
