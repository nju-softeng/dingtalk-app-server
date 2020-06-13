package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Test;
import com.softeng.dingtalk.repository.TestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhanyeye
 * @description
 * @create 5/28/2020 6:24 AM
 */
@Service
@Transactional
@Slf4j
public class TestService {
    @Autowired
    TestRepository  testRepository;

}
