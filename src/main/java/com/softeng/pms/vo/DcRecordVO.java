package com.softeng.pms.vo;

import lombok.Data;

import java.time.LocalDate;
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
    private LocalDate weekdate;
    private String auditorName;
    private int auditorid;
    private double dvalue;
    private double cvalue;
    private double ac;
    private double dc;
    private boolean status;
    private List<AcItemVO> acItems;
}
