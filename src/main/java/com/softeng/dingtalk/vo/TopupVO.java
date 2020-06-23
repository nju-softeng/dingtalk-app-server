package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhanyeye
 * @description 用来传输要更新的Topup值
 * @create 23/06/2020 7:22 AM
 */
@AllArgsConstructor
@Getter
@Setter
public class TopupVO {
    private int uid;
    private int yearmonth;
    private double topup;
}
