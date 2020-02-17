package com.softeng.dingtalk.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

/**
 * @author zhanyeye
 * @description
 * @create 2/15/2020 8:31 PM
 */
@AllArgsConstructor
@Setter
@Getter
@ToString
public class PaperinfoPO {
    String title;
    LocalTime endTime;
}
