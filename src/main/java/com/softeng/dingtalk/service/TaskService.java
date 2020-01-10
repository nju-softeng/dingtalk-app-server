package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.Task;
import com.softeng.dingtalk.entity.TaskAllocation;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.*;
import javassist.expr.NewArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    AcRecordRepository acRecordRepository;

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

    //
    public void caculateAC(Task task) {
        // 更新task
        // 查出 task 相关的 taskAllocation
        taskRepository.save(task);
        List<TaskAllocation> taskAllocations = taskAllocationRepository.findAllByTask(task);

        int day = (int) task.getBeginTime().until(task.getFinishTime(), ChronoUnit.DAYS);
        double totalAC = day * taskAllocations.size() / 30;
        double totalDC = 0;
        List<Double> dcList = new ArrayList<>();
        for (int i = 0; i < taskAllocations.size(); i++) {
            TaskAllocation taskAllocation = taskAllocations.get(i);
            String stime = task.getBeginTime().toString();
            String etime = task.getFinishTime().toString();
            double dc = dcRecordRepository.getByTime(taskAllocation.getUser().getId(), task.getAuditor().getId(), stime, etime);
            dcList.add(dc);
            log.debug(dc + "");
            totalDC += dc;
        }
        log.debug("totaldc:" + totalDC );
        for (int i = 0; i < taskAllocations.size(); i++) {
            double ac = totalAC * dcList.get(i) / totalDC * 0.5;
            log.debug(ac + "");
            TaskAllocation taskAllocation = taskAllocations.get(i);
            AcRecord acRecord = new AcRecord(taskAllocation.getUser(), task.getAuditor(), ac, task.getName());
            acRecordRepository.save(acRecord);
            taskAllocation.setAcRecord(acRecord);
            taskAllocationRepository.save(taskAllocation);
        }
    }





}
