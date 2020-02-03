package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.repository.DcSummaryRepository;
import com.softeng.dingtalk.service.ApplicationService;
import com.softeng.dingtalk.service.AuditService;
import com.softeng.dingtalk.service.UserService;
import com.softeng.dingtalk.vo.ApplicationVO;
import com.softeng.dingtalk.vo.DcSummaryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 12/11/2019 1:59 PM
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;
    @Autowired
    AuditService auditService;
    @Autowired
    UserService userService;
    @Autowired
    DingTalkUtils dingTalkUtils;
    @Autowired
    Utils utils;
    @Autowired
    DcSummaryRepository dcSummaryRepository;

    @GetMapping("/dcsummary/{yearmonth}")
    public List<DcSummaryVO>  getDcSummary(@PathVariable int yearmonth) {
        return dcSummaryRepository.listDcSummary(yearmonth);
    }


    /**
     * 返回请求中的时间时本月第几周
     * @param date
     * @return int[] 数组大小为2，第一个时yearmonth, 第二个时week
     * @Date 4:44 PM 2/3/2020
     **/
    @PostMapping("/getdate")
    public int[] getdate(@RequestBody LocalDate date) {
        return utils.getTimeFlag(date);
    }


    /**
     * @Description 用户提交申请
     * @Param [applicationDTO] 包含 dcRecord, AcItems
     * @return void
     * @Date 7:01 PM 12/27/2019
     **/
    @PostMapping("/application")
    public void addApplication(@RequestAttribute int uid, @RequestBody ApplicationVO application) {
        int[] result = utils.getTimeFlag(application.getDate()); //数组大小为2，result[0]: yearmonth, result[1] week
        DcRecord dc = new DcRecord(application, uid, result[0], result[1]);

        if (!applicationService.isExist(uid, application.getAuditorid(), result[0], result[1])) {
            applicationService.addApplication(dc, application.getAcItems());    //持久化绩效申请
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "每周只能向同一个审核人提交一次申请");
        }
    }


    /**
     * @Description 用户分页查询已提交的申请
     * @Param [uid, page]
     * @return java.util.Map
     * @Date 7:00 PM 12/27/2019
     **/
    @GetMapping("/application/{uid}/page={page}")
    public Map getUserApplication(@RequestAttribute int uid, @PathVariable int page) {
        return applicationService.getDcRecord(uid, page);
        // todo 是否要包含AcItem
    }


}
