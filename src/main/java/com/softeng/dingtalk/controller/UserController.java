package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.Message;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.service.NotifyService;
import com.softeng.dingtalk.service.UserService;
import com.softeng.dingtalk.vo.QueryUserVO;
import com.softeng.dingtalk.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    // 查询审核人
    @GetMapping("/user/getAuditors")
    public Map getAuditors() {
        return userService.getAuditorUser();
    }


    // 查询系统中所有可用用户
    @GetMapping("/userlist")
    public List<UserVO> listusers() {
        return userService.listUserVO();
    }

    // 钉钉鉴权
    @PostMapping("/jsapi_signature")
    public Map jspai(@RequestBody Map<String, String> map) {
        return dingTalkUtils.authentication(map.get("url"));
    }

    // 获取用户的消息
    @GetMapping("/message/page/{page}/{size}")
    public Map listUserMessage(@PathVariable int page, @PathVariable int size, @RequestAttribute int uid) {
        Page<Message> messages = notifyService.listUserMessage(uid, page, size);
        return Map.of("content", messages.getContent(), "total", messages.getTotalPages());
    }

//    // 查询所有用户的权限
//    @GetMapping("/listrole")
//    public List<Map<String, Object>> listRole() {
//        return userService.listRoles();
//    }


    // 更新用户权限
    @PostMapping("/updaterole")
    public void updateUserRole(@RequestBody Map<String, Object> map) {
        userService.updateRole((int) map.get("uid"), (int) map.get("authority"));
    }

    // 根据条件分页筛选用户
    @PostMapping("/user/query/{page}")
    public Map queryUser(@RequestBody QueryUserVO vo, @RequestAttribute int page) {
        Page<User> pages = userService.multiQueryUser(page, vo.getName(), vo.getPosition());
        return Map.of("content", pages.getContent(), "total", pages.getTotalPages());
    }



}
