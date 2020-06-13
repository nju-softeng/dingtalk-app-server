package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.Test;
import com.softeng.dingtalk.enums.PaperType;
import com.softeng.dingtalk.enums.Position;
import com.softeng.dingtalk.repository.TestRepository;
import com.softeng.dingtalk.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    SystemService systemService;

    @GetMapping("/test/enums")
    public PaperType test_enums() {
        return PaperType.CONFERENCE_A;
    }


    @PostMapping("/test/post")
    public PaperType getTest1(@RequestBody Test test) {
        log.debug(test.getPaperType().toString());
        return test.getPaperType();
    }


    @GetMapping("/test/query_enums")
    public Test getTest() {
        return testRepository.findById(1).get();
    }




    @GetMapping("/test/sub1")
    public double test1() {
        return systemService.getSubsidy(Position.OTHER);
    }

    @GetMapping("/test/sub2")
    public void test2() {

    }

}
