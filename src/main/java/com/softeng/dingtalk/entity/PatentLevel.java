package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "patent_level")
public class PatentLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 级别名称
     */
    private String title;

    /**
     * 最高可获得的AC
     */
    @Column(columnDefinition="DECIMAL(10,3)")
    private double total;

    public PatentLevel(String title, double total) {
        this.title = title;
        this.total = total;
    }
}
