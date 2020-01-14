package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 1/10/2020 8:38 PM
 */

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;


    /**
     * 获取用户信息, 用户登录后调用
     * @param uid
     * @return java.util.Map
     * @Date 4:57 PM 1/13/2020
     **/
    @GetMapping("/user/info")
    public Map getInfo(@RequestAttribute int uid) {
        return userService.getUserInfo(uid);
    }

    @GetMapping("/user/getAuditors")
    public Map getAuditors() {
        return userService.getAuditorUser();
    }
}
