package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.convertor.UserConvertor;
import com.softeng.dingtalk.component.encryptor.EncryptorComponent;
import com.softeng.dingtalk.component.dingApi.ContactsApi;
import com.softeng.dingtalk.component.UserContextHolder;
import com.softeng.dingtalk.dto.CommonResult;
import com.softeng.dingtalk.dto.resp.UserResp;
import com.softeng.dingtalk.po_entity.User;
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

    //为了防止伪造角色, 已经废弃
    private static final String USER_ROLE = "bb63e5f7e0f2ffae845c";
    private static final String AUDITOR_ROLE = "pb53e2f7g0f2hfanp4sx";
    private static final String ADMIN_ROLE = "6983f953b49c88210cb9";

    @Autowired
    ContactsApi contactsApi;
    @Autowired
    UserService userService;
    @Autowired
    SystemService systemService;
    @Autowired
    EncryptorComponent encryptorComponent;
    @Resource
    UserContextHolder userContextHolder;


    /**
     * 开发环境下登陆（废弃）
     * @param uid
     * @param response
     */
    @Deprecated
    @GetMapping("/login_test/{uid}")
    public void testlogin(@PathVariable int uid, HttpServletResponse response) {
        log.debug("测试登陆" + uid);
        Map map = Map.of("uid", uid, "authorityid", User.NORMAL_AUTHORITY);
        // 生成加密token
        String token = encryptorComponent.encrypt(map);
        // 在header创建自定义的权限
        response.setHeader("token",token);
        response.setHeader("role", ADMIN_ROLE);
        response.setHeader("uid", uid + "");
    }

    /**
     * @description 用户登录(废弃)
     * @param authcode：免登授权码
     * @return java.util.Map
     * @date 9:17 AM 12/11/2019
     **/
    @Deprecated
    @PostMapping("/login")
    public Map login(@RequestBody Map authcode, HttpServletResponse response) {
        //根据免登授权码获取userid
        log.debug(authcode.toString());
        String userid = contactsApi.getUserId((String) authcode.get("authCode"));
        //去数据库查找用户

        log.debug("userid:" + userid);
        User user = userService.getUser(userid);
        if (user == null) {
            //如果用户不存在，调用钉钉API获取用户信息，将用户导入数据库
            user = systemService.addNewUser(userid);
        }
        Map map = Map.of("uid", user.getId(), "authorityid", user.getAuthority());
        // 生成加密token
        String token = encryptorComponent.encrypt(map);
        // 在header创建自定义的权限
        response.setHeader("token",token);
        String role = null;
        if (user.getAuthority() == User.NORMAL_AUTHORITY) {
            role = USER_ROLE;
        } else if (user.getAuthority() == User.AUDITOR_AUTHORITY) {
            role = AUDITOR_ROLE;
        } else {
            role = ADMIN_ROLE;
        }
        response.setHeader("role", role);
        response.setHeader("uid", user.getId() + "");
        return Map.of("role", role, "uid", user.getId(), "token", token);
    }

    /**
     * 开发环境下登陆
     * @param uid
     * @param response
     */
    @GetMapping("/v2/login_test/{uid}")
    public CommonResult<Map> testlogin2(@PathVariable int uid, HttpServletResponse response) {
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
        response.setHeader("uid", uid + "");
        return CommonResult.success(Map.of("token",token, "uid", uid + ""));
    }


    /**
     * @description 用户登录
     * @param authcode：免登授权码
     * @return java.util.Map
     * @date 9:17 AM 12/11/2019
     **/
    @PostMapping("/v2/login")
    public Map login2(@RequestBody Map authcode, HttpServletResponse response) {
        //根据免登授权码获取userid
        log.debug(authcode.toString());
        String userid = contactsApi.getUserId((String) authcode.get("authCode"));
        //去数据库查找用户

        log.debug("userid:" + userid);
        User user = userService.getUser(userid);
        if (user == null) {
            //如果用户不存在，调用钉钉API获取用户信息，将用户导入数据库
            user = systemService.addNewUser(userid);
        }
        int uid = user.getId();
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
