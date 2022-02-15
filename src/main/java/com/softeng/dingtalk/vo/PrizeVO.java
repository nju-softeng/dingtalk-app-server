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
public class PrizeVO {
    private int id;
    private int userId;
    private LocalDate prizeTime;
    private String prizeName;
    private int level;
    private String remark;
    private boolean deleted;
}
