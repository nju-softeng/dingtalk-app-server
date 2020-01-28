package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.dto.ApplicationDTO;
import com.softeng.dingtalk.dto.AuditDTO;
import com.softeng.dingtalk.service.AuditService;
import com.softeng.dingtalk.vo.ApplicationVO;
import com.softeng.dingtalk.vo.ReviewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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


    /**
     * 审核员提交审核结果
     * @param  reviewVO 审核结果信息
     * @return void
     * @date 9:35 AM 12/27/2019
     **/
    @PostMapping("/audit")
    public void addAuditResult(@RequestBody ReviewVO reviewVO) {
        auditService.addAuditResult(reviewVO); //持久化审核结果
    }

    /**
     * 审核人获取待审核的申请
     * @param uid
     * @return java.util.List<com.softeng.dingtalk.dto.ApplicationInfo>
     * @Date 10:06 AM 12/28/2019
     **/
    @GetMapping("/pending_audit")
    public List<ApplicationVO> getAuditApplication(@RequestAttribute int uid) {
        log.debug("/pending_audit" + uid);
        return auditService.getPendingApplication(uid);
    }


    @GetMapping("/getreport")
    public List<Object> getReport(@RequestAttribute int uid) {
        try {
            return auditService.AsyncGetReport(uid);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "并发请求超时");
        }
    }
}
