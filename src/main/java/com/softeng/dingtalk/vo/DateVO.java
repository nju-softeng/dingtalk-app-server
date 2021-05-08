package com.softeng.dingtalk.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author zhanyeye
 * @description
 * @create 2/7/2020 2:59 PM
 */
@Setter
@Getter
@ToString
public class DateVO {
    /**
     * 查询的日期
     */
    private LocalDate date;
    /**
     * 数据的排序方式
     * true:  降序
     * false: 升序
     */
    private boolean desc;
}
