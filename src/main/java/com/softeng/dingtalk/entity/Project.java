package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    private String title; // 项目名
    private int successCnt; // 连续按时交付数
    private int curIteration; // 该项目当前迭代版本id
    private int cnt; // 当前迭代次数
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private User auditor;

//    private String name;
//    private String description;
//    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
//    private User auditor;
//    private int num;
//    @Column(columnDefinition="DECIMAL(10,3)")
//    private double expectedAC;
//    private boolean status;
//    private LocalDate beginTime;
//    private LocalDate endTime;
//    private LocalDate finishTime;
//    @JsonIgnoreProperties("project")
//    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
//    private List<ProjectDetail> projectDetails;
//
//    public Project(int id) {
//        this.id = id;
//    }
//
//    public Project(String name, User auditor, LocalDate beginTime, LocalDate endTime) {
//        this.name = name;
//        this.auditor = auditor;
//        this.beginTime = beginTime;
//        this.endTime = endTime;
//    }

}
