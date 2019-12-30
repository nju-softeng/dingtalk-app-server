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
    private double dvalue;  // Dedication Value
    private double cvalue;  // Contribution Value
    private double dc;    //
    private int week;
    private boolean ischeck;  // 是否被审核
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;

    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private User applicant;
    @ManyToOne(fetch = FetchType.LAZY)
    private User auditor;

    public DcRecord(double dc, LocalDateTime createTime, int week, User applicant, User auditor, Application application) {
        this.dc = dc;
        this.insertTime = insertTime;
        this.week = week;
        this.applicant = applicant;
        this.auditor = auditor;
    }
}
