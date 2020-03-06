package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanyeye
 * @description  UserService测试类
 * @date 12/7/2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void test_addUser() {
      //  User u = new User("0001", "zhanyeye", "avatar", 0);
        //userService.addUser(u);
    }


    @Test
    public void test_getUserid() {
        log.debug(userService.getUserid(2));
    }
}
