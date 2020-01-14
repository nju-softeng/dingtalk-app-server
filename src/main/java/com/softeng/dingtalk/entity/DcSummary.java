package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private double week1;
    private double week2;
    private double week3;
    private double week4;
    private double week5;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public double computeTotal() {
        return this.week1 + this.week2 + this.week3 + this.week4 + this.week5;
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
    }

    public DcSummary(User user, int yearmonth) {
        this.yearmonth = yearmonth;
        this.user = user;
    }

}
