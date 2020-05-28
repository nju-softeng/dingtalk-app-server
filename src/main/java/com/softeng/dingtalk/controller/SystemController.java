package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.enums.Position;
import com.softeng.dingtalk.service.NotifyService;
import com.softeng.dingtalk.service.SystemService;
import com.softeng.dingtalk.service.UserService;
import com.softeng.dingtalk.vo.QueryUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 5/27/2020 4:49 PM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class SystemController {
    @Autowired
    SystemService systemService;

    /**
     * 根据条件分页筛选用户
     * @param vo
     * @param page
     * @return
     */
    @PostMapping("/system/user/{page}")
    public Map queryUser(@RequestBody QueryUserVO vo, @PathVariable int page) {
        Page<User> pages = systemService.multiQueryUser(page, 10, vo.getName(), vo.getPosition());
        return Map.of("content", pages.getContent(), "total", pages.getTotalElements());
    }

    /**
     *
     */
    @GetMapping("/system/fetchuser")
    public void fetchUser() {
        systemService.fetchUsers();
    }





}
