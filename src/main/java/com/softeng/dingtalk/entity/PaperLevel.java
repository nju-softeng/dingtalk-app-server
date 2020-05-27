package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    /**
     * 级别名称
     */
    private String title;
    /**
     * 数字代号表示
     */
    private int level;
    /**
     * 最高可获得的AC
     */
    @Column(columnDefinition="DECIMAL(10,3)")
    private double total;

    public PaperLevel(String title, int level, double total) {
        this.title = title;
        this.level = level;
        this.total = total;
    }

}
