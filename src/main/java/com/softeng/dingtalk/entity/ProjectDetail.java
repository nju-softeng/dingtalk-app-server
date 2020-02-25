package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description
 * @create 2/25/2020 2:42 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class ProjectDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private Project project; //任务
    @ManyToOne(fetch = FetchType.LAZY)
    private User user; //开发者
    @ManyToOne(fetch = FetchType.LAZY)
    private AcRecord acRecord; //获得的ac值
    private double ac;

    public ProjectDetail(Project project, User user) {
        this.project = project;
        this.user = user;
    }

}
