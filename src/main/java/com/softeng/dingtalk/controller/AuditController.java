package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.dto.ApplicationInfo;
import com.softeng.dingtalk.dto.AuditInfo;
import com.softeng.dingtalk.entity.Application;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
     * @description
     * @param [dcRecord]
     * @return void
     * @date 9:35 AM 12/27/2019
     **/
    @PostMapping("/audit")
    public void addAuditResult(@RequestBody AuditInfo auditInfo) {
        auditService.addAuditResult(auditInfo);
    }

    //审核人获取待审核的申请
    @GetMapping("/pending_audit/{uid}")
    public List<ApplicationInfo> getAuditApplication(@RequestAttribute int uid) {
        return auditService.getPendingApplication(uid);
    }
}
