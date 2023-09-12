package com.softeng.dingtalk.dao.mapper;

import com.softeng.dingtalk.dto.resp.excelData.AcData;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;

@Repository
public interface AcRecordMapper {
    List<AcData> listAcDataByYearMonth(int year, Month month);
}
