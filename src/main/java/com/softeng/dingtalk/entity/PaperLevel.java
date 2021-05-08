package com.softeng.dingtalk.entity;

import com.softeng.dingtalk.enums.PaperType;
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
     * 论文类别
     */
    @Enumerated(EnumType.STRING)
    private PaperType paperType;

    /**
     * 最高可获得的AC
     */
    @Column(columnDefinition="DECIMAL(10,3)")
    private double total;

    public PaperLevel(String title, PaperType paperType, double total) {
        this.title = title;
        this.paperType = paperType;
        this.total = total;
    }

}
