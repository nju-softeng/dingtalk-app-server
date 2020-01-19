package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description
 * @create 1/19/2020 8:33 PM
 */
@AllArgsConstructor
@Getter
public class DcRecordVO {
    private int id;
    private String name;
    private double dvalue;  // Dedication Value
    private int yearmonth;  // 表示申请所属 年、月
    private int week;       // 申请所属周
    private LocalDateTime insertTime;
}
