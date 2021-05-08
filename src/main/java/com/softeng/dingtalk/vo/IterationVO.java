package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 3/14/2020 11:11 PM
 */
@AllArgsConstructor
@Getter
@Setter
public class IterationVO {
    private int id;
    private String title;
    private LocalDate[] dates;
    private List<String> dingIds;
    private boolean updateDingIds;
}
