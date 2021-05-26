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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ExcelController {

    @Autowired
    AcRecordMapper acRecordMapper;

    @PostMapping("/excel/ac_data")
    public void downAcData(@RequestBody LocalDate date, HttpServletResponse response) throws IOException {
        String title = date.toString().substring(0, 7);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + title + ".xlsx");

        List<AcData> acDataList = acRecordMapper.listAcDataByYearMonth(date.getYear(), date.getMonth());

        try {
            EasyExcel.write(response.getOutputStream(), AcData.class).sheet("模板").doWrite(acDataList);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("导出excel失败{}", e.getMessage());
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("下载文件失败: " + e.getMessage());
        }
    }
}
