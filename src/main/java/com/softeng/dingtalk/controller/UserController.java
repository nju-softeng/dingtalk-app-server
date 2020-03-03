package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.Message;
import com.softeng.dingtalk.service.NotifyService;
import com.softeng.dingtalk.service.UserService;
import com.softeng.dingtalk.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @Autowired
    DingTalkUtils dingTalkUtils;
    @Autowired
    NotifyService notifyService;


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

    @GetMapping("/userlist")
    public List<UserVO> listusers() {
        return userService.listUserVO();
    }

    // 钉钉鉴权
    @PostMapping("/jsapi_signature")
    public Map jspai(@RequestBody Map<String, String> map) {
        return dingTalkUtils.authentication(map.get("url"));
    }

    @GetMapping("/message/page/{page}")
    public Map listUserMessage(@PathVariable int page, @RequestAttribute int uid) {
        Slice<Message> messages = notifyService.listUserMessage(uid, page);
        return Map.of("content", messages.getContent());
    }



}
