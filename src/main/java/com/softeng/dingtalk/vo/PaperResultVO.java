package com.softeng.dingtalk.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaperResultVO {
    private boolean result;
    private LocalDate updateDate;
}
