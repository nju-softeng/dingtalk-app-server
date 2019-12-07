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
@RequestMapping("/api")
public class LoginController {

    @Autowired
    DingTalkUtils dingTalkUtils;


    @GetMapping("/getuserid/{code}")
    public Map getUserId(@PathVariable String code) {
        String userid = dingTalkUtils.getUserId(code);
        return Map.of("userid", userid);
    }

    @PostMapping("/login")
    public Map login(@RequestBody Map authcode) {
        String userid = dingTalkUtils.getUserId((String) authcode.get("code"));
        /**
         *  判断
         **/
        log.debug(userid);
        return dingTalkUtils.getUserDetail(userid);
    }

}
