package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.dto.CommonResult;
import com.softeng.dingtalk.dto.req.InternshipPeriodRecommendedReq;
import com.softeng.dingtalk.dto.resp.InternshipPeriodRecommendedResp;
import com.softeng.dingtalk.service.InternshipPeriodRecommendedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/api/v2")
public class InternshipPeriodRecommendedController {

    @Resource
    private InternshipPeriodRecommendedService internshipPeriodRecommendedService;

    @PostMapping("/internshipPeriodRecommended")
    public CommonResult<String> addPeriod(@RequestBody InternshipPeriodRecommendedReq internshipPeriodRecommendedReq) {
        internshipPeriodRecommendedService.addPeriod(internshipPeriodRecommendedReq);
        return CommonResult.success("添加成功");
    }

    @GetMapping ("/internshipPeriodRecommended")
    public CommonResult<InternshipPeriodRecommendedResp> getNewestPeriod() {
        return CommonResult.success(internshipPeriodRecommendedService.getNewestPeriod());
    }
}
