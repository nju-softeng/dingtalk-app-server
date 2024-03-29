package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.enums.LongitudinalLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 2/25/2020 2:15 PM
 */

@AllArgsConstructor
@Getter
@Setter
public class ProjectVO {
    private int id;
    private String name;
    private int auditorid;
    private LocalDate[] dates;
    private List<String> dingIds;
    private boolean updateDingIds;

    /**
     * @Description
     * @Author Jerrian Zhao
     * @Data 01/28/2022
     */
    private int leaderId;
    private boolean nature;
    private char horizontalLevel;
    private LongitudinalLevel longitudinalLevel;
}
