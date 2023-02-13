package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.EncryptorComponent;
import com.softeng.dingtalk.api.ContactsApi;
import com.softeng.dingtalk.component.UserContextHolder;
import com.softeng.dingtalk.po.UserPo;
import com.softeng.dingtalk.service.SystemService;
import com.softeng.dingtalk.service.UserService;
import com.softeng.dingtalk.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
    ContactsApi contactsApi;
    @Autowired
    UserService userService;
    @Autowired
    SystemService systemService;
    @Resource
    UserContextHolder userContextHolder;

    /**
     * 开发环境下登陆
     * @param uid
     * @param response
     */
    @GetMapping("/login_test/{uid}")
    public void testlogin(@PathVariable int uid, HttpServletResponse response) {
        log.debug("测试登陆" + uid);
        UserContextHolder.UserContext userContext = new UserContextHolder.UserContext()
                .setUid(uid)
                .setPermissionIds(StreamUtils.map(
                        userService.getPermissions(uid),
                        permission -> permission.getId()
                ));
        // 生成加密token
        String token = userContextHolder.encrypt(userContext);
        // 在header创建自定义的权限
        response.setHeader("token",token);
//        response.setHeader("role", ADMIN_ROLE);
        response.setHeader("uid", uid + "");
    }


    /**
     * @description 用户登录
     * @param authcode：免登授权码
     * @return java.util.Map
     * @date 9:17 AM 12/11/2019
     **/
    @PostMapping("/login")
    public Map login(@RequestBody Map authcode, HttpServletResponse response) {
        //根据免登授权码获取userid
        log.debug(authcode.toString());
        String userid = contactsApi.getUserId((String) authcode.get("authCode"));
        //去数据库查找用户

        log.debug("userid:" + userid);
        UserPo userPo = userService.getUser(userid);
        if (userPo == null) {
            //如果用户不存在，调用钉钉API获取用户信息，将用户导入数据库
            userPo = systemService.addNewUser(userid);
        }
        int uid = userPo.getId();
        UserContextHolder.UserContext userContext = new UserContextHolder.UserContext()
                .setUid(uid)
                .setPermissionIds(StreamUtils.map(
                        userService.getPermissions(uid),
                        permission -> permission.getId()
                ));
        // 生成加密token
        String token = userContextHolder.encrypt(userContext);
        // 在header创建自定义的权限
        response.setHeader("token",token);
        response.setHeader("uid", uid + "");
        return Map.of( "uid", uid, "token", token);
    }
}
