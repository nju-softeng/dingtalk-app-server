package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.service.DcSummaryService;
import com.softeng.dingtalk.vo.DateVO;
import com.softeng.dingtalk.vo.DcSummaryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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
    DcSummaryService dcSummaryService;

    @PostMapping("/dcsummary")
    public List<DcSummaryVO> getDcSummary(@RequestBody DateVO dateVO) {
        log.debug(dateVO.toString());
        return dcSummaryService.listDcSummaryVO(dateVO.getDate());
    }


}
