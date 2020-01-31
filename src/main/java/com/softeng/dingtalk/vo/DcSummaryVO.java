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
    private DcSummary dcSummary;
}
