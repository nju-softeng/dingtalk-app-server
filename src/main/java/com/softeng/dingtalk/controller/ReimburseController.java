package com.softeng.dingtalk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class ReimburseController {
    @PostMapping("/reimburse")
    public void addReimbursement(){

    }
}
