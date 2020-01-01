package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description 汇总每周的绩效值
 * @create 12/29/2019 9:26 AM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class DcSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int yearmonth;
    private int week;
    private double dc;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public DcSummary(int yearmonth, int week, double dc, User user) {
        this.yearmonth = yearmonth;
        this.week = week;
        this.dc = dc;
        this.user = user;
    }

}
