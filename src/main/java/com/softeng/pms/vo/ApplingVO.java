package com.softeng.pms.vo;

import com.softeng.pms.entity.AcItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
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
    @Max(value = 1, message = " D值不能大于 1！")
    private double dvalue;
    private double ac;
    /**
     * ac值申请列表
     */
    private List<AcItem> acItems;    
}
