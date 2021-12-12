package com.softeng.dingtalk.mapper;

import com.softeng.dingtalk.excel.DcSummaryData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcSummaryMapper {
    List<DcSummaryData> listDcSummaryDataByYearMonth(int yearmonth);
}
