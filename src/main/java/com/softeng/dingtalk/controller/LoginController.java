package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.DingTalkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @date 11/13/2019
 */
@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    DingTalkUtils dingTalkUtils;

    @GetMapping("/getuserid/{code}")
    public Map getUserId(@PathVariable String code) {
        String userid = null;
        try {
            userid = dingTalkUtils.getUserId(code);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "code 过期");
        }
        return Map.of("userid", userid);
    }


}
