package com.softeng.dingtalk.component;

import com.softeng.dingtalk.component.encryptor.EncryptorComponent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: LiXiaoKang
 * @CreateTime: 2023-02-09
 * @Description: 拦截器保存用户信息，切面验证权限后销毁
 * @Version: 1.0
 */

@Slf4j
@Component
public class UserContextHolder {

    @Resource
    EncryptorComponent encryptorComponent;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class UserContext {
        /**
         * 对应user表的id字段
         */
        private Integer uid;
        /**
         * 这里的权限不仅包括permission表中的权限，还包括其他业务权限（例如评审，是否是是该组成员等）
         */
        private List<Integer> permissionIds;
    }
    private final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public UserContext getUserContext() {
        return userContext.get();
    }

    public void setUserContext(UserContext userContext) {
        this.userContext.set(userContext);
    }

    public void  remove() {
        this.userContext.remove();
    }

    public UserContext decrypt(String str) {
        return encryptorComponent.decrypt(str, UserContextHolder.UserContext.class);
    }

    public String encrypt(UserContext userContext) {
        return encryptorComponent.encrypt(userContext);
    }
}
