package com.softeng.dingtalk.dao.mapper;

import com.softeng.dingtalk.excel.AcData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;
import java.util.List;

@Repository
public interface AcRecordMapper {
    List<AcData> listAcDataByYearMonth(int year, Month month);
}
