package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description 任务分配信息
 * @create 1/3/2020 6:28 PM
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
public class TaskAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private Task task; //任务
    @ManyToOne(fetch = FetchType.LAZY)
    private User user; //开发者
    @ManyToOne(fetch = FetchType.LAZY)
    private AcRecord acRecord; //获得的ac值
    private double ac;


    public TaskAllocation(Task task, User user) {
        this.task = task;
        this.user = user;
    }
}

