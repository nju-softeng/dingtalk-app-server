package com.softeng.dingtalk.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * @author zhanyeye
 * @description
 * @create 2/15/2020 8:31 PM
 */
@AllArgsConstructor
@Setter
@Getter
public class Paperinfo1PO {
    String title;
    LocalTime endTime;
}
