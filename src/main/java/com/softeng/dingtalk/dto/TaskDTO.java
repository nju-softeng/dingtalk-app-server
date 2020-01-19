package com.softeng.dingtalk.dto;

import com.softeng.dingtalk.entity.Task;
import com.softeng.dingtalk.entity.TaskAllocation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhanyeye
 * @description 封装任务和任务分配信息
 * @create 1/4/2020 2:20 PM
 */
@Getter
@Setter
public class TaskDTO {
    private Task task;
    private int[] uids;
}
