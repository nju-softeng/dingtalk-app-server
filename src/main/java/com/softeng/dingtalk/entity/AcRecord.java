package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description AC值变更记录
 * @date 12/5/2019
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class AcRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int month;
    private int week;
    private int AC;
    private int totalAC;
    private String reason;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;

    @ManyToOne
    private User user;
    @ManyToOne
    private User auditor;

}
