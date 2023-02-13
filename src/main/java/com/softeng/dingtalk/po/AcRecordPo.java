package com.softeng.dingtalk.po;

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
@ToString
@Table(name = "ac_record")
public class AcRecordPo {

    public static final int NORMAL = 0;
    public static final int PROJECT = 1;
    public static final int PAPER = 2;
    public static final int VOTE = 3;
    public static final int BUG = 4;
    public static final int Patent = 5;
    public static final int DingTalkSchedule = 6;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double ac;
    private String reason;
    private int classify;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = true)
    private LocalDateTime createTime;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserPo user;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserPo auditor;

    public AcRecordPo(UserPo user, UserPo auditor, double ac, String reason, int classify, LocalDateTime createTime) {
        this.user = user;
        this.auditor = auditor;
        this.ac = ac;
        this.reason = reason;
        this.classify = classify;
        this.createTime = createTime;
    }

    public AcRecordPo(DcRecordPo dcRecordPO, AcItemPo acItemPO, LocalDateTime createTime) {
        this.ac = acItemPO.getAc();
        this.reason = acItemPO.getReason();
        this.user = dcRecordPO.getApplicant();
        this.auditor = dcRecordPO.getAuditor();
        this.createTime = createTime;
    }
}
