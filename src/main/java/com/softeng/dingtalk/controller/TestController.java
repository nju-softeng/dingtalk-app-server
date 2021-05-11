package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.api.BaseApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    BaseApi baseApi;

    @GetMapping("/test/getAccessToken")
    public String testGetAccessToken() {
        return baseApi.getAccessToken();
    }

    @GetMapping("/test/getJsapiTicket")
    public String testGetJsapiTicket() {
        return baseApi.getJsapiTicket();
    }
}
