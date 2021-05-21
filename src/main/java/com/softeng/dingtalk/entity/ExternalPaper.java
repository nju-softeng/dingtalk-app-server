package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @description:
 * @author: zhanyeye
 * @create: 2020-10-21 20:07
 **/
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ExternalPaper implements Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * 论文标题
     */
    private String title;
    /**
     * 该外部论文的最终录用结果
     */
    private Boolean result;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private Vote vote;

    /**
     * 更新时间
     */
    private LocalDate updateDate;

    public ExternalPaper(String title) {
        this.title = title;
    }

    @Override
    public boolean isExternal() {
        return true;
    }
}
