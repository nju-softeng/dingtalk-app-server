package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.DcSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhanyeye
 * @description
 * @create 1/29/2020 2:19 PM
 */
@AllArgsConstructor
@Getter
@Setter
public class DcSummaryVO {
    private String name;
    private int yearmonth;
    private double week1;
    private double week2;
    private double week3;
    private double week4;
    private double week5;
    private double total;
}
