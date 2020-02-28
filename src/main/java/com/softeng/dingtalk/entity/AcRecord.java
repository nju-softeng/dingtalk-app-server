package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description AC值记录 (AC日志：被审核人确认的用户ac变更记录)
 * @date 12/5/2019
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class AcRecord {

    public static final int NORMAL = 0;
    public static final int PROJECT = 1;
    public static final int PAPER = 2;
    public static final int VOTE = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double ac;
    private String reason;
    private int classify;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime createTime;
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private User auditor;

    public AcRecord(User user, User auditor, double ac, String reason) {
        this.user = user;
        this.auditor = auditor;
        this.ac = ac;
        this.reason = reason;
    }

    public AcRecord(User user, double ac, String reason) {
        this.user = user;
        this.ac = ac;
        this.reason = reason;
    }

    public AcRecord(DcRecord dcRecord, AcItem acItem) {
        this.ac = acItem.getAc();
        this.reason = acItem.getReason();
        this.user = dcRecord.getApplicant();
        this.auditor = dcRecord.getAuditor();
    }
}
