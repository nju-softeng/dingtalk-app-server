package com.softeng.dingtalk.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author zhanyeye
 * @description
 * @create 2/7/2020 2:59 PM
 */
@Setter
@Getter
@ToString
public class DateVO {
    private LocalDate date;
}
