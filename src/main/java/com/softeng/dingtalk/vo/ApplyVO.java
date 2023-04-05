package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.po_entity.AcItem;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 4/1/2020 5:48 PM
 */
@Getter
@Setter
public class ApplyVO {
    private int id;
    private int auditorid;
    private LocalDate date;
    @Max(value = 1, message = " D值不能大于 1！")
    private double dvalue;
    @Max(value = 1, message = " C值不能大于 1！")
    private double cvalue;
    private double ac;
    /**
     * ac值申请列表
     */
    private List<AcItem> acItems;
}
