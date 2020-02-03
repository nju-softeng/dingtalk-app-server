package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.AcItem;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description 申请人获取已经提交的申请
 * @create 2/3/2020 11:12 PM
 */
public class AppliedVO {
    private int id;
    private boolean status;
    private int auditorid;
    private LocalDate date;
    private double dvalue;
    private List<AcItem> acItems;     //ac值申请列表
}
