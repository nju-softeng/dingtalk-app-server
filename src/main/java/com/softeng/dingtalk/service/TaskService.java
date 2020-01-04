package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Task;
import com.softeng.dingtalk.entity.TaskAllocation;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.TaskAllocationRepository;
import com.softeng.dingtalk.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 1/4/2020 9:43 AM
 */
@Service
@Transactional
@Slf4j
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskAllocationRepository taskAllocationRepository;

    /**
     * 审核人创建任务，分配开发同学
     * @param task, taskAllocations
     * @return void
     * @Date 3:48 PM 1/4/2020
     **/
    public void addTask(Task task, int[] uids) {
        taskRepository.save(task);
        log.debug(uids.length + "?");
        for (int i = 0; i < uids.length; i++) {
            taskAllocationRepository.save(new TaskAllocation(task, new User(uids[i])));
        }
    }

    /**
     * 审核人修改任务信息
     * @param task
     * @param uids  uid 数组，为空表示不修改task的分配情况
     * @return void
     * @Date 4:51 PM 1/4/2020
     **/
    public void updateTask(Task task, int[] uids) {
        taskRepository.save(task);
        if (uids.length  != 0) {
            taskAllocationRepository.deleteByTask(task);
            for (int i = 0; i < uids.length; i++) {
                taskAllocationRepository.save(new TaskAllocation(task, new User(uids[i])));
            }
        }
    }





}
