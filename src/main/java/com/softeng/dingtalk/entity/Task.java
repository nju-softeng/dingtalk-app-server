package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author zhanyeye
 * @description 迭代任务信息
 * @create 1/2/2020 1:44 PM
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private User auditor;
    private int num;
    private double expectedAC;
    private boolean isAchieve;
    private LocalDate beginTime;
    private LocalDate deadline;
    private LocalDate finishTime;

    public Task(String name) {
        this.name = name;
    }
}
