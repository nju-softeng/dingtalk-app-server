package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhanyeye
 * @description  UserService测试类
 * @date 12/7/2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void test_addUser() {
        User u = new User("0001", "zhanyeye", "avatar", 0);
        userService.addUser(u);
    }
}
