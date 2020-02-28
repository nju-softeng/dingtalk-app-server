package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.service.PerformanceService;
import com.softeng.dingtalk.vo.DateVO;
import com.softeng.dingtalk.vo.DcSummaryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    // 拿到 dc 汇总
    @PostMapping("/dcsummary")
    public List<Map<String, Object>> getDcSummary(@RequestBody DateVO dateVO) {
        log.debug(dateVO.toString());
        return performanceService.listDcSummaryVO(dateVO.getDate());
    }

    // 拿到 ac 汇总
    @GetMapping("/acsummary")
    public List<Map<String, Object>> getAcSummary() {
        return performanceService.listAcSummary();
    }

    @GetMapping("userac/{uid}")
    public List<Map<String, Object>> listUserAc(@PathVariable int uid) {
        return performanceService.listUserAc(uid);

    }






}
