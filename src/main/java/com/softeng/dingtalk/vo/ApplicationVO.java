package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.DcRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description 申请人每周的提交的绩效申请，包含dvalue, 和 acItems
 * @create 2/2/2020 2:17 PM
 */
public class ApplicationVO {
    private LocalDate date;
    private double dvalue;  // Dedication Value
    private int yearmonth;  // 表示申请所属 年、月
    private int week;       // 申请所属周
    private List<AcItem> acItems;     //ac值申请列表
}
