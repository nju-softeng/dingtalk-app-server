package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description 每周绩效申请中的AC值申请
 * @date 12/5/2019
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class AcItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int ac;
    private String reason;

    @ManyToOne
    private Application application;  //ac申请属于的周绩效申请
}
