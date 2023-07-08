package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.DateUtils;
import com.softeng.dingtalk.dao.repository.DcSummaryRepository;
import com.softeng.dingtalk.service.ApplicationService;
import com.softeng.dingtalk.service.AuditService;
import com.softeng.dingtalk.service.UserService;
import com.softeng.dingtalk.vo.ApplyVO;
import com.softeng.dingtalk.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
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
    DateUtils dateUtils;
    @Autowired
    DcSummaryRepository dcSummaryRepository;

    /**
     * 返回请求中的时间是本月第几周
     * @param date
     * @return int[] 数组长度为 2，第一个元素表示：yearmonth, 第二个元素表示：week
     */
    @PostMapping("/date_code")
    public int[] getDate(@RequestBody LocalDate date) {
        int dateCode = dateUtils.getDateCode(date);
        int yearmonth = dateCode / 10;
        int week = dateCode % 10;
        return new int[] {yearmonth, week};
    }

    /**
     * 提交新的绩效申请
     * @param uid 申请人的 id
     * @param vo  申请的绩效内容
     */
    @PostMapping("/application")
    public void addApplication(@RequestAttribute int uid, @Valid @RequestBody ApplyVO vo) {
        if (uid == vo.getAuditorid()) {
            applicationService.addApplicationByAuditor(vo, uid);
        } else {
            applicationService.addApplication(vo, uid);
        }
    }

    /**
     * 跟新已经提交的申请
     * @param uid
     * @param vo
     */
    @PutMapping("/application/{id}")
    public void updateApplication(@RequestAttribute int uid, @Valid @RequestBody ApplyVO vo) {
        if (uid == vo.getAuditorid()) {
            applicationService.updateApplicationByAuditor(vo, uid);
        } else {
            applicationService.updateApplication(vo, uid);
        }
    }

    /**
     * 用户分页查询已提交的申请
     * @param uid
     * @param page
     * @return
     */
    @GetMapping("/application/page/{page}/size/{size}")
    public Map getUserApplication(@RequestAttribute int uid, @PathVariable int page, @PathVariable int size) {
        return applicationService.listDcRecord(uid, page, size);
    }

    /**
     * 查询申请人最近一次绩效申请的审核人是谁
     * @param uid 申请人id
     * @return
     */
    @GetMapping("/application/recent_auditor/{uid}")
    public UserVO findLatestAuditor(@PathVariable int uid) {
        return applicationService.getRecentAuditor(uid);
    }

}
