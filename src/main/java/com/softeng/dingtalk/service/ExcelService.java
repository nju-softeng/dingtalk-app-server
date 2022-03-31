package com.softeng.dingtalk.service;

import com.alibaba.excel.EasyExcel;
import com.softeng.dingtalk.entity.DcSummary;
import com.softeng.dingtalk.excel.AcData;
import com.softeng.dingtalk.excel.DcSummaryData;
import com.softeng.dingtalk.excel.UserPrize;
import com.softeng.dingtalk.excel.UserProperty;
import com.softeng.dingtalk.mapper.AcRecordMapper;
import com.softeng.dingtalk.mapper.DcSummaryMapper;
import com.softeng.dingtalk.repository.DcSummaryRepository;
import com.softeng.dingtalk.repository.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class ExcelService {

    @Autowired
    AcRecordMapper acRecordMapper;
    @Autowired
    DcSummaryMapper dcSummaryMapper;
    @Autowired
    PropertyRepository propertyRepository;

    /**
     * 根据日期，下载指定月份所有同学AC变化的情况
     * @param date 需要下载的日期
     * @param outputStream response的输出流
     */
    public void writeAcDataByDate(LocalDate date, OutputStream outputStream) {
        EasyExcel.write(outputStream, AcData.class)
                .sheet(date.toString().substring(0, 7))
                .doWrite(acRecordMapper.listAcDataByYearMonth(date.getYear(), date.getMonth()));
    }


    /**
     * 根据日期，下载指定月份所有同学绩效情况的情况
     * @param date 需要下载的日期
     * @param outputStream response的输出流
     */
    public void writeDcSummaryByDate(LocalDate date, OutputStream outputStream) {
        int yearmonth = date.getYear() * 100 + date.getMonthValue();
        EasyExcel.write(outputStream, DcSummaryData.class)
                .sheet(date.toString().substring(0, 7))
                .doWrite(dcSummaryMapper.listDcSummaryDataByYearMonth(yearmonth));
    }

    public void writeUserPropertyDataByDate(OutputStream outputStream){
        EasyExcel.write(outputStream, UserProperty.class)
                .sheet("用户固定资产")
                .doWrite(propertyRepository.findAll().stream()
                        .map(property -> new UserProperty(property.getUser().getStuNum(),property.getPreserver(),
                                property.getName(),property.getType(),property.getStartTime().toString())).collect(Collectors.toList()));
    }

    public void writeUserPrizeDataByDate(OutputStream outputStream){

    }


}
