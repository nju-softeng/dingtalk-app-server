package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.AcRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 1/28/2020 8:49 AM
 */
@Setter
@Getter
@AllArgsConstructor
public class ReviewVO {
    private int id;
    private double cvalue;
    private double dc;
    private double ac;
    private List<AcItem> acItems;
}
