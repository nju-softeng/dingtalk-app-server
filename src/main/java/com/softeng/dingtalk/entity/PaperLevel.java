package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author zhanyeye
 * @description
 * @create 2/5/2020 10:49 PM
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PaperLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;  // 级别名称
    private int level;     // 数字代号表示
    private double total;  // 最高可获得的AC

    public PaperLevel(String title, int level, double total) {
        this.title = title;
        this.level = level;
        this.total = total;
    }

}
