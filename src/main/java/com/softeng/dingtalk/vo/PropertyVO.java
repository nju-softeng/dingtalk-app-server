package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @Description
 * @Author Jerrian Zhao
 * @Data 01/24/2022
 */
@Setter
@Getter
@AllArgsConstructor
public class PropertyVO {
    private int id;
    private int userId;
    private String name;
    private String type;
    private String preserver;
    private LocalDate startTime;
    private String remark;
    private boolean deleted;
}
