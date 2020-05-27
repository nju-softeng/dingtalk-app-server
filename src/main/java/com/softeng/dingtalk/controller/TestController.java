package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.enums.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanyeye
 * @description
 * @create 5/27/2020 11:19 PM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/test/enums")
    public Position test_enums() {
        return Position.DOCTOR;
    }

}
