package com.softeng.dingtalk.mapper;

import com.softeng.dingtalk.excel.AcData;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;
import java.util.List;

@Repository
public interface AcRecordMapper {
    List<AcData> listAcDataByYearMonth(int year, Month month);
}
