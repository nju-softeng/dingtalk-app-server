package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.repository.DcSummaryRepository;
import com.softeng.dingtalk.service.ApplicationService;
import com.softeng.dingtalk.service.AuditService;
import com.softeng.dingtalk.service.UserService;
import com.softeng.dingtalk.vo.ApplyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
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


    /**
     * 返回请求中的时间时本月第几周
     * @param date
     * @return int[] 数组大小为2，第一个时yearmonth, 第二个时week
     */
    @PostMapping("/getdate")
    public int[] getdate(@RequestBody LocalDate date) {
        return utils.getTimeFlag(date);
    }


    /**
     * 提交或更新dc申请
     * @param uid
     * @param vo
     */
    @PostMapping("/application")
    public DcRecord submitApplication(@RequestAttribute int uid, @Valid @RequestBody ApplyVO vo) {
        return applicationService.submitApplication(vo, uid);
    }


    /**
     * 用户分页查询已提交的申请
     * @param uid
     * @param page
     * @return
     */
    @GetMapping("/application/page/{page}/{size}")
    public Map getUserApplication(@RequestAttribute int uid, @PathVariable int page, @PathVariable int size) {
        return applicationService.listDcRecord(uid, page, size);
    }


}
