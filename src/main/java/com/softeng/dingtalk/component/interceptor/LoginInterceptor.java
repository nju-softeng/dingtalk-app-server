package com.softeng.dingtalk.component.interceptor;

import com.softeng.dingtalk.component.encryptor.EncryptorComponent;
import com.softeng.dingtalk.component.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author zhanyeye
 * @description 登录拦截器
 * @date 12/8/2019
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
//    @Autowired
//    private EncryptorComponent encryptorComponent;
    @Resource
    private UserContextHolder userContextHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String tmp = request.getHeader("token");
        Optional.ofNullable(request.getHeader("token"))
                .ifPresentOrElse(token -> {
                    // 如果token存在，则将token 解密后 里面的 uid 和 aid 塞入，request 请求中
                    // 这个token 是在客户端第一次登录(向LoginController发请求)，登录成功后
                    // LoginController 封装 uid 和 aid 到repsonse响应，给客户端，用于后续客户端发送的请求的声明识别

                    /**
                     * @Deprecated: 废弃做法
                     */
//                    var token_map = encryptorComponent.decrypt(token);
//                    request.setAttribute("uid", token_map.get("uid")); //塞入 用户id
//                    request.setAttribute("aid", token_map.get("aid")); //塞入 用户权限值


                    /**
                     * 改用userContextHolder保存登录信息（uid与permissionId）
                     */
                    userContextHolder.setUserContext(userContextHolder.decrypt(token));
                    request.setAttribute("uid", userContextHolder.getUserContext().getUid());
                }, () -> {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "未登录！");
                });
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        userContextHolder.remove();
    }
}