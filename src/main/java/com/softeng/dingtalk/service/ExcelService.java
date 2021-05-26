package com.softeng.dingtalk.service;

import com.alibaba.excel.EasyExcel;
import com.softeng.dingtalk.excel.AcData;
import com.softeng.dingtalk.mapper.AcRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.time.LocalDate;


@Service
@Transactional
@Slf4j
public class ExcelService {
    @Autowired
    AcRecordMapper acRecordMapper;

    public void WriteAcDataByDate(LocalDate date, OutputStream outputStream) {
        EasyExcel.write(outputStream, AcData.class).sheet(date.toString().substring(0, 7)).doWrite(acRecordMapper.listAcDataByYearMonth(LocalDate.now().getYear(), LocalDate.now().getMonth()));
    }


}
