package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description 周绩效申请中被审核人确认的AC记录（一个 application 可能会有多个 acRecord, 也可能没有关系）
 * @create 12/28/2019 8:43 AM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class ApplicationAcRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AcRecord acRecord;
}
