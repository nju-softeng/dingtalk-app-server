package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.dto.TaskInfo;
import com.softeng.dingtalk.entity.Task;
import com.softeng.dingtalk.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhanyeye
 * @description
 * @create 1/4/2020 1:28 PM
 */

@Slf4j
@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    TaskService taskService;

    /**
     * 审核人创建任务
     * @param taskInfo
     * @return void
     * @Date 2:44 PM 1/4/2020
     **/
    @PostMapping("/task")
    public void addTask(@RequestBody TaskInfo taskInfo) {
        taskService.addTask(taskInfo.getTask(), taskInfo.getUids());
    }


    /**
     * 审核人更新任务
     * @param taskInfo
     * @return void
     * @Date 5:05 PM 1/4/2020
     **/
    @PatchMapping("/task")
    public void updateTask(@RequestBody TaskInfo taskInfo) {
        taskService.updateTask(taskInfo.getTask(), taskInfo.getUids());
    }




}
