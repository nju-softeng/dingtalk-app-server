package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.dto.TaskDTO;
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
     * @param taskDTO
     * @return void
     * @Date 2:44 PM 1/4/2020
     **/
    @PostMapping("/task")
    public void addTask(@RequestBody TaskDTO taskDTO) {
        taskService.addTask(taskDTO.getTask(), taskDTO.getUids());
    }


    /**
     * 审核人更新任务
     * @param taskDTO
     * @return void
     * @Date 5:05 PM 1/4/2020
     **/
    @PatchMapping("/task")
    public void updateTask(@RequestBody TaskDTO taskDTO) {
        taskService.updateTask(taskDTO.getTask(), taskDTO.getUids());
    }


    /**
     * 计算任务的ac？？ //todo 待确认
     * @param task
     * @return void
     * @Date 11:26 PM 1/4/2020
     **/
    @PostMapping("/task_ac")
    public void caculateAC(@RequestBody Task task) {
        taskService.caculateAC(task);
    }

}
