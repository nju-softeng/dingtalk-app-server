package com.softeng.dingtalk.controller;

import com.alibaba.excel.EasyExcel;
import com.softeng.dingtalk.excel.AcData;
import com.softeng.dingtalk.mapper.AcRecordMapper;
import com.softeng.dingtalk.service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api")
public class ExcelController {

    @Autowired
    AcRecordMapper acRecordMapper;

    @PostMapping("/excel/ac_data")
    public void downAcData(@RequestBody LocalDate date, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + title + ".xlsx");

        List<AcData> acDataList = acRecordMapper.listAcDataByYearMonth(date.getYear(), date.getMonth());

        try {
            excelService.WriteAcDataByDate(date, response.getOutputStream());
        } catch (Exception e) {
            log.info("导出excel失败：{}", e.getMessage());
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("下载文件失败: " + e.getMessage());
        }
    }
}
