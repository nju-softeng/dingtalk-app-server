package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.service.PaperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhanyeye
 * @description
 * @create 2/5/2020 4:14 PM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class PaperController {
    @Autowired
    PaperService paperService;

    @PostMapping
    public void addPaper(@RequestBody Paper paper) {

    }


}
