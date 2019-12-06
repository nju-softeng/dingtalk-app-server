package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description  记录用户当周的申过的审核人列表，确保一周只能向审核人申请1次，避免在 Application 上创建4个字段的联合唯一约束，定时器每周清空表
 * @date 12/5/2019
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class WeeklyApplyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private User applicant;
    @ManyToOne
    private User auditor;
}
