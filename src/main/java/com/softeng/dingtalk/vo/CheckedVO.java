package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.DcRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 向前端传送审核人已经审核的申请,向后端传送审核人修改的申请
 * @create 1/28/2020 8:11 PM
 */
@AllArgsConstructor
@NoArgsConstructor  // 序列化用
@Getter
@Setter
public class CheckedVO {
    private int id;         // dcRecord id
    private String name;    // 申请人姓名
    private int uid;        // 申请人id
    private double dvalue;  // Dedication Value
    private double cvalue;  // Contribution Value
    private double dc;
    private double ac;
    private int yearmonth;  // 表示申请所属 年、月
    private int week;
    private LocalDateTime insertTime;
    private List<AcItem> acItems;

    public CheckedVO(DcRecord dc, List<AcItem> acItems) {
        this.id = dc.getId();
        this.name = dc.getApplicant().getName();
        this.uid = dc.getApplicant().getId();
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
