package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description 所有的确认的DC值记录（DC日志）
 * @date 12/5/2019
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class DcRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int month;
    private int week;
    private double dc;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;

    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private User applicant;
    @ManyToOne(fetch = FetchType.LAZY)
    private User auditor;

    @OneToOne
    @JoinColumn(unique = true)
    private Application application;
}
