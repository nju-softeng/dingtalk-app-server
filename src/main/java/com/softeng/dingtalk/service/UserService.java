package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author zhanyeye
 * @description User业务逻辑组件
 * @date 12/7/2019
 */
@Service
@Transactional
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUser(String userid) {
        return userRepository.findUserByUserid(userid);
    }

    public User addUser(User user) {
        User u = userRepository.save(user);
        return userRepository.refresh(u);
    }
}
