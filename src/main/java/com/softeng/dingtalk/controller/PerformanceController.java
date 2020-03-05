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

    // 查询 dc 汇总
    @PostMapping("/dcsummary")
    public List<Map<String, Object>> getDcSummary(@RequestBody DateVO dateVO) {
        log.debug(dateVO.toString());
        return performanceService.listDcSummaryVO(dateVO.getDate());
    }


    // 查询 ac 汇总
    @GetMapping("/acsummary")
    public List<Map<String, Object>> getAcSummary() {
        return performanceService.listAcSummary();
    }


    // 查询指定用户的所有 AC
    @GetMapping("/userac/{uid}")
    public List<Map<String, Object>> listUserAc(@PathVariable int uid) {
        return performanceService.listUserAc(uid);
    }


    // 查询最近的10条AC变更
    @GetMapping("/lastac")
    public List<Map<String, Object>> listLastAc() {
        return performanceService.listLastAc();
    }


    // 获取首页的绩效信息
    @GetMapping("/performance")
    public Map getUserPerformance(@RequestAttribute int uid) {
        return performanceService.getUserPerformace(uid);
    }





}
