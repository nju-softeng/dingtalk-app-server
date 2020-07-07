package com.softeng.dingtalk.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 07/07/2020 11:25 AM
 */
@Data
public class DcRecordVO {
    private int id;
    private int yearmonth;
    private int week;
    private LocalDateTime insertTime;
    private String auditorName;
    private double dvalue;
    private double cvalue;
    private double ac;
    private double dc;
    private boolean status;
    private List<AcItemVO> acItems;
}
