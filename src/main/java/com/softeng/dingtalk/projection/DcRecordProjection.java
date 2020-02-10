package com.softeng.dingtalk.projection;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description DcRecord 投影，向前端传送限定的属性
 * @date 2/9/2020
 */
public interface DcRecordProjection {
    Integer getId();
    Double getDvalue();
    Double getCvalue();
    Double getDc();
    Double getAc();
    Boolean getStatus();
    Integer getYearmonth();
    Integer getWeek();
    LocalDate getWeekdate();
    LocalDateTime getInsertTime();
    List<AcItem> getAcItems();
    User getAuditor();
}
