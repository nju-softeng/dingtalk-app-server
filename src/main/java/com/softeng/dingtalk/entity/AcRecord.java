package com.softeng.dingtalk.entity;

import lombok.*;

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
@AllArgsConstructor
@Builder
public class AcRecord {

    public static final int NORMAL = 0;
    public static final int PROJECT = 1;
    public static final int PAPER = 2;
    public static final int VOTE = 3;
    public static final int BUG = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double ac;
    private String reason;
    private int classify;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = true)
    private LocalDateTime createTime;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private User auditor;

    public AcRecord(User user, User auditor, double ac, String reason, int classify) {
        this.user = user;
        this.auditor = auditor;
        this.ac = ac;
        this.reason = reason;
        this.classify = classify;
    }

    public AcRecord(User user, double ac, String reason, int classify) {
        this.user = user;
        this.ac = ac;
        this.reason = reason;
        this.classify = classify;
    }

    public AcRecord(DcRecord dcRecord, AcItem acItem) {
        this.ac = acItem.getAc();
        this.reason = acItem.getReason();
        this.user = dcRecord.getApplicant();
        this.auditor = dcRecord.getAuditor();
    }
}
