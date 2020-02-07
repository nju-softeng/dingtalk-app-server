package com.softeng.dingtalk.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description
 * @create 1/23/2020 11:46 AM
 */
@AllArgsConstructor
@Setter
@Getter
public class ReportApplicantPO {
    private String userid;
    private int uid;
    private LocalDate date;
}
