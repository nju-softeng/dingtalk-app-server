package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description
 * @create 1/22/2020 2:31 PM
 */
@AllArgsConstructor
@Getter
public class ReportInfoVO {
    int uid;
    LocalDateTime dateTime;
}
