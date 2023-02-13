package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.po.AcItemPo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 审核人获取待审核申请数据
 * @create 1/19/2020 8:33 PM
 */
@AllArgsConstructor
@Getter
@Setter
public class ToCheckVO {
    private int id;
    private int uid;
    private String name;
    /**
     * Dedication Value
     */
    private double dvalue;
    /**
     * 表示申请所属 年、月
     */
    private int yearmonth;
    /**
     * 申请所属周
     */
    private int week;
    private LocalDateTime insertTime;
    List<AcItemPo> acItemPos;
    private LocalDate weekdate;

    public ToCheckVO(int id, int uid, String name, double dvalue, int yearmonth, int week, LocalDateTime insertTime, LocalDate weekdate) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.dvalue = dvalue;
        this.yearmonth = yearmonth;
        this.week = week;
        this.insertTime = insertTime;
        this.weekdate = weekdate;
    }
}
