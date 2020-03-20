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


}
