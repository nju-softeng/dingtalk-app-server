package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.service.AuditService;
import com.softeng.dingtalk.service.UserService;
import com.softeng.dingtalk.vo.CheckedVO;
import com.softeng.dingtalk.vo.CheckVO;
import com.softeng.dingtalk.vo.ToCheckVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
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
    DingTalkUtils dingTalkUtils;
    @Autowired
    UserService userService;
    @Autowired
    Utils utils;

    /**
     * 审核员提交审核结果
     * @param  checkVO 审核结果信息
     * @return void
     * @date 9:35 AM 12/27/2019
     **/
    @PostMapping("/audit")
    public void addAuditResult(@Valid @RequestBody CheckVO checkVO) {
        log.debug("/audit" );

        DcRecord dc = auditService.addAuditResult(checkVO); //持久化审核结果
        auditService.updateDcSummary(dc);
    }


    @GetMapping("/audit/uncheckcnt")
    public int  getUnCheckCnt(@RequestAttribute int uid) {
        return auditService.getUnCheckCnt(uid);
    }



    /**
     * 审核人更新审核记录
     * @param checkVO
     * @return void
     * @Date 9:13 PM 2/1/2020
     **/
    @PostMapping("/audit/update")
    public void updateChecked(@Valid @RequestBody CheckVO checkVO) {
        log.debug("/updateAudit" );

        DcRecord dc = auditService.updateAudit(checkVO); //持久化审核结果
        auditService.updateDcSummary(dc);

    }

    /**
     * 审核人获取待审核的申请
     * @param uid
     * @return java.util.List<com.softeng.dingtalk.dto.ApplicationInfo>
     * @Date 10:06 AM 12/28/2019
     **/
    @GetMapping("/audit/pending")
    public List<ToCheckVO> getAuditApplication(@RequestAttribute int uid) {
        log.debug("/pending_audit uid:" + uid);
        return auditService.getPendingApplication(uid);
    }

    /**
     * 审核人获取已经审核的申请
     * @param uid
     * @return java.util.List<com.softeng.dingtalk.vo.CheckedVO>
     * @Date 8:41 PM 1/29/2020
     **/
    @GetMapping("/audit/checked")
    public List<CheckedVO> listChecked(@RequestAttribute int uid) {
        return auditService.listCheckVO(uid);
    }

    // 审核人根据时间筛选已经审核的申请
    @PostMapping("/audit/checked/date")
    public List<CheckedVO> listCheckedByDate(@RequestAttribute int uid, @RequestBody Map<String, LocalDate> map) {
        int[] date = utils.getTimeFlag(map.get("date"));
        int yearmonth = date[0];
        int week = date[1];
        return auditService.listCheckedByDate(uid, yearmonth, week);
    }


    /**
     * 根据uid和时间获取周报
     * @param uid, date
     * @return java.util.Map
     * @Date 9:12 PM 2/4/2020
     **/
    @PostMapping("/audit/report/{uid}")
    public Map getReport(@PathVariable int uid, @RequestBody Map<String, LocalDate> map) {
        String userid = userService.getUserid(uid);
        return dingTalkUtils.getReport(userid, map.get("date"));
    }

}
