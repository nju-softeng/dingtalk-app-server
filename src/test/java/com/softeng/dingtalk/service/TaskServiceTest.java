package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Task;
import com.softeng.dingtalk.entity.TaskAllocation;
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
 * @description
 * @create 1/4/2020 1:48 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TaskServiceTest {
    @Autowired
    TaskService taskService;

    @Test
    public void test_addTask() {

    }
}
