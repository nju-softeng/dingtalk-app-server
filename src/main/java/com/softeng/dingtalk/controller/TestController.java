package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.Test;
import com.softeng.dingtalk.enums.Position;
import com.softeng.dingtalk.repository.TestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    TestRepository testRepository;

    @GetMapping("/test/enums")
    public Position test_enums() {
        return Position.DOCTOR;
    }

    @GetMapping("/test/query_enums")
    public Test getTest() {
        return testRepository.findById(1).get();
    }


    @GetMapping("/test/query_enums_str")
    public Position getTest1() {
        return testRepository.test();
    }

}
