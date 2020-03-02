package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.DcRecord;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description 申请人每周的提交的绩效申请，包含dvalue, 和 acItems 等
 * @create 2/2/2020 2:17 PM
*/
@Getter
@Setter
@ToString
public class ApplingVO {
    private int id;
    private int auditorid;
    private LocalDate date;
    private double dvalue;
    private double ac;
    private List<AcItem> acItems;     //ac值申请列表
}
