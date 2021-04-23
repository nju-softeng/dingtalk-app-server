package com.softeng.pms.vo;

import com.softeng.pms.entity.AcItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import java.util.List;

/**
 * @author zhanyeye
 * @description 审核人提交的审核结果
 * @create 1/28/2020 8:49 AM
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
public class CheckVO {
    private int id;
    private double cvalue;
    @Max(value = 1, message = " DC 值不能大于 1！")
    private double dc;
    private double ac;
    private List<AcItem> acItems;
}
