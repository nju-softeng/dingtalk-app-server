package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.DingTalkUtils;
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

    @PostMapping("/test")
    public void test(@RequestBody LocalDate date) {
        log.debug(date.toString());
    }

    /**
     * 审核人更新审核记录
     * @param checked
     * @return void
     * @Date 9:13 PM 2/1/2020
     **/
    @PostMapping("/updateAudit")
    public void updateChecked(@RequestBody CheckedVO checked) {
        log.debug("/updateAudit" );
        auditService.updateAudit(checked);
    }

    /**
     * 审核人获取已经审核的申请
     * @param uid
     * @return java.util.List<com.softeng.dingtalk.vo.CheckedVO>
     * @Date 8:41 PM 1/29/2020
     **/
    @GetMapping("/checked")
    public List<CheckedVO> listChecked(@RequestAttribute int uid) {
        return auditService.listCheckVO(uid);
    }

    /**
     * 审核员提交审核结果
     * @param  checkVO 审核结果信息
     * @return void
     * @date 9:35 AM 12/27/2019
     **/
    @PostMapping("/audit")
    public void addAuditResult(@RequestBody CheckVO checkVO) {
        auditService.addAuditResult(checkVO); //持久化审核结果
    }

    /**
     * 审核人获取待审核的申请
     * @param uid
     * @return java.util.List<com.softeng.dingtalk.dto.ApplicationInfo>
     * @Date 10:06 AM 12/28/2019
     **/
    @GetMapping("/pending_audit")
    public List<ToCheckVO> getAuditApplication(@RequestAttribute int uid) {
        log.debug("/pending_audit" + uid);
        return auditService.getPendingApplication(uid);
    }


    @GetMapping("/getreportlist")
    public List<Object> getReportList(@RequestAttribute int uid) {
        try {
            return auditService.AsyncGetReport(uid);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "并发请求超时");
        }
    }

    @PostMapping("/getreport/{uid}")
    public Map getReport(@PathVariable int uid, @RequestBody LocalDate date) {
        String userid = userService.getUserid(uid);
        return dingTalkUtils.getReport(userid, date);
    }

}
