package com.softeng.dingtalk.dao.mapper;

import com.softeng.dingtalk.dto.resp.excelData.DcSummaryData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcSummaryMapper {
    List<DcSummaryData> listDcSummaryDataByYearMonth(int yearmonth);
}
