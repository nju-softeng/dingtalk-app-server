package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.Task;
import com.softeng.dingtalk.entity.TaskAllocation;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.*;
import javassist.expr.NewArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        if (uids.length  != 0) { //如果uids 不为空，则认为任务分配情况没有改变
            taskAllocationRepository.deleteByTask(task);
            for (int i = 0; i < uids.length; i++) {
                taskAllocationRepository.save(new TaskAllocation(task, new User(uids[i])));
            }
        }
    }

    /**
     * 计算ac值
     * 实际ac计算公式: 𝐴_𝑖=𝐴_𝑎∗𝐷_𝑖/(∑𝐷)∗𝐷_𝑖/0.5
     * 𝐴_𝑖 denotes individual actual reward
     * 𝐴_𝑎 denotes team acutal reward
     * 𝐷_𝑖  denotes individual average DC during the iteration
     * @param task :finishTime 需要已经设置
     * @return void
     * @Date 5:38 PM 1/13/2020
     **/
    public void caculateAC(Task task) {
        // todo 计算延时
        List<TaskAllocation> taskAllocations = taskAllocationRepository.findAllByTask(task); //获取该任务的分配记录
        int day = (int) task.getBeginTime().until(task.getFinishTime(), ChronoUnit.DAYS);  //完成任务所花费时间
        double totalAC = day * taskAllocations.size() / 30; // 总ac值 = 实际时间 * 参与人数 / 30
        double totalDC = 0; // 各参与者开发周期内的dc值求和
        double[] dcList = new double[taskAllocations.size()]; // 记录各参与者开发周期内的dc值
        int index = 0;
        for (TaskAllocation t : taskAllocations) {
            //获取指定用户，指定审核人，指定时间段的dc和
            double dc = dcRecordRepository.getByTime(t.getUser().getId(), task.getAuditor().getId(), task.getBeginTime().toString(), task.getFinishTime().toString());
            dcList[index++] = dc;
            log.debug(dc + "");
            totalDC += dc;
        }
        log.debug("totaldc:" + totalDC );

        if (totalDC == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "项目参与者的总dc值为0，可能参与者未提交dc申请，无法计算，需人工决定");
        }

        index = 0;
        for (TaskAllocation t : taskAllocations) {
            double ac = totalAC * dcList[index++] / totalDC * 0.5; // 计算该用户在项目中的实际ac
            log.debug("个人实际ac: " + ac);
            AcRecord acRecord = new AcRecord(t.getUser(), task.getAuditor(), ac, task.getName());
            acRecordRepository.save(acRecord); // 实例化ac记录
            t.setAcRecord(acRecord);
            taskAllocationRepository.save(t);
        }
    }





}
