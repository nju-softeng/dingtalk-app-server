package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author zhanyeye
 * @description
 * @create 2/25/2020 11:52 AM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private User auditor;
    private int num;
    private double expectedAC;
    private boolean status;
    private LocalDate beginTime;
    private LocalDate endTime;
    private LocalDate finishTime;

    public Project(String name, User auditor, LocalDate beginTime, LocalDate endTime) {
        this.name = name;
        this.auditor = auditor;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

}
