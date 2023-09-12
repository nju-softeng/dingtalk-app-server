package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api")
public class ExcelController {

    @Autowired
    ExcelService excelService;

    @PostMapping("/excel/ac_data")
    public void downloadAcData(@RequestBody LocalDate date, HttpServletResponse response) throws IOException {
//        response.setContentType("application/vnd.ms-excel;chartset=utf-8"); //文件扩展名为excel格式
//        response.setHeader("Content-Disposition", "attachment;filename=" + "fileName.xlsx");
        excelService.writeAcDataByDate(date, response.getOutputStream());
    }


    @PostMapping("/excel/dc_summary_data")
    public void downloadDcSummaryData(@RequestBody LocalDate date, HttpServletResponse response) throws IOException {
        excelService.writeDcSummaryByDate(date, response.getOutputStream());
    }

    @PostMapping("/excel/userProperty_data")
    public void downloadUserPropertyData(HttpServletResponse response) throws IOException {
        excelService.writeUserPropertyDataByDate(response.getOutputStream());
    }

    @PostMapping("/excel/userPrize_data")
    public void downloadUserPrizeData(HttpServletResponse response) throws IOException {
        excelService.writeUserPrizeDataByDate(response.getOutputStream());
    }

}
