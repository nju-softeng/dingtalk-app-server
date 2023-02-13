package com.softeng.dingtalk.po;

import com.softeng.dingtalk.enums.LongitudinalLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description
 * @create 2/25/2020 11:52 AM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "project")
public class ProjectPo {
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

    @ManyToOne(fetch = FetchType.LAZY)
    private UserPo auditor;

    /**
     * @Description 新增项目属性
     * @Author Jerrian Zhao
     * @Data 01/28/2022
     */

    /**
     * 负责人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private UserPo leader;

    /**
     * 项目性质
     * true为横向 false为纵向
     */
    @Column(nullable = false)
    private Boolean nature;

    /**
     * 横向项目级别
     */
    private char horizontalLevel;

    /**
     * 纵向项目级别
     */
    private LongitudinalLevel LongitudinalLevel;
}
