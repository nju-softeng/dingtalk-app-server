package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.service.PerformanceService;
import com.softeng.dingtalk.vo.DateVO;
import com.softeng.dingtalk.vo.DcSummaryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 2/7/2020 12:52 PM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class PerformanceController {
    @Autowired
    PerformanceService performanceService;

    @PostMapping("/dcsummary")
    public List<DcSummaryVO> getDcSummary(@RequestBody DateVO dateVO) {
        log.debug(dateVO.toString());
        return performanceService.listDcSummaryVO(dateVO.getDate());
    }

    @GetMapping("/acsummary")
    public List<Object> getAcSummary() {
        return performanceService.listAcSummary();
    }




}
