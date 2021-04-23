package com.softeng.pms.controller;

import com.softeng.pms.component.DingTalkUtils;
import com.softeng.pms.entity.Message;
import com.softeng.pms.service.NotifyService;
import com.softeng.pms.service.UserService;
import com.softeng.pms.vo.UserInfoVO;
import com.softeng.pms.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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


    /**
     * 查询所有审核人
     * @return
     */
    @GetMapping("/user/getAuditors")
    public Map getAuditors() {
        return userService.getAuditorUser();
    }


    /**
     * 查询系统中所有可用用户
     * @return
     */
    @GetMapping("/userlist")
    public List<UserVO> listusers() {
        return userService.listUserVO();
    }


    /**
     * 钉钉鉴权
     * @param map
     * @return
     */
    @PostMapping("/jsapi_signature")
    public Map jspai(@RequestBody Map<String, String> map) {
        return dingTalkUtils.authentication(map.get("url"));
    }


    /**
     * 获取用户的消息
     * @param page
     * @param size
     * @param uid
     * @return
     */
    @GetMapping("/message/page/{page}/{size}")
    public Map listUserMessage(@PathVariable int page, @PathVariable int size, @RequestAttribute int uid) {
        Page<Message> messages = notifyService.listUserMessage(uid, page, size);
        return Map.of("content", messages.getContent(), "total", messages.getTotalElements());
    }


    /**
     * 更新用户权限
     * @param map
     */
    @PostMapping("/updaterole")
    public void updateUserRole(@RequestBody Map<String, Object> map) {
        userService.updateRole((int) map.get("uid"), (int) map.get("authority"));
    }


    /**
     * 获取用户信息
     * @param uid
     * @return
     */
    @GetMapping("/user/detail")
    public UserInfoVO getUserDetail(@RequestAttribute int uid){
        return userService.getUserDetail(uid);
    }


    @PostMapping("/user/update")
    public void updateUserInfo(@RequestBody UserInfoVO userInfoVO, @RequestAttribute int uid) {
        userService.updateUserInfo(userInfoVO, uid);
    }


}
