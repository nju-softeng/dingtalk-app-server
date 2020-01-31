package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.DcRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 向前端传送审核人已经审核的申请
 * @create 1/28/2020 8:11 PM
 */
@AllArgsConstructor
@Getter
public class CheckedVO {
    private String name;
    private int id;
    private double dvalue;  // Dedication Value
    private double cvalue;  // Contribution Value
    private double dc;
    private double ac;
    private int yearmonth;  // 表示申请所属 年、月
    private int week;
    private LocalDateTime insertTime;
    private List<AcItem> acItems;

    public CheckedVO(String name, DcRecord dc, List<AcItem> acItems) {
        this.name = name;
        this.id = dc.getId();
        this.dvalue = dc.getDvalue();
        this.cvalue = dc.getCvalue();
        this.dc = dc.getDc();
        this.ac = dc.getAc();
        this.yearmonth = dc.getYearmonth();
        this.week = dc.getWeek();
        this.insertTime = dc.getInsertTime();
        this.acItems = acItems;
    }
}
