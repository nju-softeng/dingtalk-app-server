package com.softeng.pms.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 6:01 PM
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VoteVO {
    private int paperid;
    private LocalTime endTime;
}
