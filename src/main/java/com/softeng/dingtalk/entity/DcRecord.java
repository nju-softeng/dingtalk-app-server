package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description 所有的确认的 DC值 记录
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
    private int DC;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;

    @ManyToOne
    private User user;
    @ManyToOne
    private User auditor;
}
