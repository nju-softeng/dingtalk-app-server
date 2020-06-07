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
    /**
     * 项目名
     */
    private String title;
    /**
     * 连续按时交付数
     */
    private int successCnt;
    /**
     * 该项目当前迭代版本id
     */
    private int curIteration;
    /**
     * 当前迭代次数
     */
    private int cnt;

    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private User auditor;


}
