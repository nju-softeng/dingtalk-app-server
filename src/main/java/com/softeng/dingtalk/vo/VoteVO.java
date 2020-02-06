package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 6:01 PM
 */
@AllArgsConstructor
@Getter
@Setter
public class VoteVO {
    int paperid;
    private LocalTime startTime;
    private LocalTime endTime;
}
