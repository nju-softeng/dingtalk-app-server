package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.service.BugService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanyeye
 * @description
 * @create 3/12/2020 6:40 PM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class BugController {
    @Autowired
    BugService bugService;



}
