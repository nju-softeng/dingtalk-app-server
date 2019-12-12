package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.DcRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanyeye
 * @description
 * @create 12/12/2019 10:50 AM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class AuditController {
    @PostMapping("/audit")
    public void addDcRecord(@RequestBody DcRecord dcRecord) {
        //TODO
        //TODO
        //TODO
    }
}
