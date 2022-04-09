package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.DateUtils;
import com.softeng.dingtalk.api.ReportApi;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.service.AuditService;
import com.softeng.dingtalk.service.NotifyService;
import com.softeng.dingtalk.service.UserService;
import com.softeng.dingtalk.vo.CheckedVO;
import com.softeng.dingtalk.vo.CheckVO;
import com.softeng.dingtalk.vo.ToCheckVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 12/12/2019 10:50 AM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class AuditController {
    @Autowired
    AuditService auditService;
    @Autowired
    UserService userService;
    @Autowired
    DateUtils dateUtils;
    @Autowired
    NotifyService notifyService;

    @Autowired
    ReportApi reportApi;

    /**
     * 审核员提交审核结果, 刷新dcsummary, 应用内发送消息
     * @param  checkVO 审核结果信息
     * @return void
     * @date 9:35 AM 12/27/2019
     **/
    @PostMapping("/audit")
    public void submitAuditResult(@Valid @RequestBody CheckVO checkVO) {
        auditService.updateAuditResult(checkVO);
    }


    /**
     * 未审核申请数量
     * @param uid
     * @return
     */
    @GetMapping("/audit/uncheck_cnt")
    public int  getUnCheckCnt(@RequestAttribute int uid) {
        return auditService.getUnCheckCnt(uid);
    }


    /**
     * 审核人获取待审核的申请
     * @param uid
     * @return
     */
    @GetMapping("/audit/pending")
    public List<ToCheckVO> getAuditApplication(@RequestAttribute int uid) {
        return auditService.listPendingApplication(uid);
    }


    /**
     * 审核人获取已经审核的申请
     * @param uid
     * @return java.util.List<com.softeng.dingtalk.vo.CheckedVO>
     * @Date 8:41 PM 1/29/2020
     **/
    @GetMapping("/audit/checked/page/{page}")
    public Map listChecked(@RequestAttribute int uid, @PathVariable int page) {
        return auditService.listCheckedVO(uid, page, 10);
    }


    /**
     * 审核人根据时间筛选已经审核的申请
     * @param uid
     * @param date
     * @return
     */
    @PostMapping("/audit/checked/date")
    public List<CheckedVO> listCheckedByDate(@RequestAttribute int uid, @RequestBody LocalDate date) {
        int dateCode = dateUtils.getDateCode(date);
        int yearmonth = dateCode / 10;
        int week = dateCode % 10;
        return auditService.listCheckedByDate(uid, yearmonth, week);
    }


    /**
     * 根据uid和时间获取周报
     * 获取指定日期当天的一条周报
     * @param uid, date
     * @return java.util.Map
     **/
    @PostMapping("/audit/report/{uid}")
    public Map getReport(@PathVariable int uid, @RequestBody LocalDate date) {
        LocalDateTime startTime = date.atTime(0, 0);
        return reportApi.getReport(userService.getUserid(uid),
                startTime,
                startTime.plusDays(1));
    }

}
