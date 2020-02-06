package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.DcSummary;
import com.softeng.dingtalk.repository.DcSummaryRepository;
import com.softeng.dingtalk.service.DcSummaryService;
import com.softeng.dingtalk.vo.DcSummaryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 9:42 AM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class DcSummaryController {
    @Autowired
    DcSummaryService dcSummaryService;

    @PostMapping("/dcsummary")
    public List<DcSummaryVO> getDcSummary(@RequestBody LocalDate date) {
        return dcSummaryService.listDcSummaryVO(date);
    }

}
