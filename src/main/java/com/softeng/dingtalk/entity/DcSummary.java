package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanyeye
 * @description 汇总每月各周绩效值
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
    //表示用户当月第几周的 DC值
    @Column(columnDefinition="DECIMAL(10,3)")
    private double week1;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double week2;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double week3;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double week4;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double week5;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double total;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double ac;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double topup;
    @Column(columnDefinition="DECIMAL(10)")
    private double salary;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // Gain = Base * DC * (1+ (AC/50)) + Topup
    public void computeTotal() {
        List<BigDecimal> weeks = new ArrayList<>();
        weeks.add(BigDecimal.valueOf(week1));
        weeks.add(BigDecimal.valueOf(week2));
        weeks.add(BigDecimal.valueOf(week3));
        weeks.add(BigDecimal.valueOf(week4));
        weeks.add(BigDecimal.valueOf(week5));
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal w : weeks) {
            sum = sum.add(w);
        }
        total = sum.doubleValue();
    }



    /**
     *  设置本月第 week 周的dc值
     * @param week, dc
     * @return void
     * @Date 9:13 PM 1/2/2020
     **/
    public void updateWeek(int week, double dc) {
        switch (week) {
            case 1: this.week1 = dc;break;
            case 2: this.week2 = dc;break;
            case 3: this.week3 = dc;break;
            case 4: this.week4 = dc;break;
            case 5: this.week5 = dc;break;
        }
        computeTotal();
    }

    public DcSummary(User user, int yearmonth) {
        this.yearmonth = yearmonth;
        this.user = user;
    }

    public DcSummary(int uid, int yearmonth) {
        this.yearmonth = yearmonth;
        this.user = new User(uid);
    }

}
