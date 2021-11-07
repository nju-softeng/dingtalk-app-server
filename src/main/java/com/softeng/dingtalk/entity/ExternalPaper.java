package com.softeng.dingtalk.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: zhanyeye
 * @create: 2020-10-21 20:07
 **/
@Entity
@Data
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

    /**
     * 插入时间
     */
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;

    public ExternalPaper(String title) {
        this.title = title;
    }

    @Override
    public boolean isExternal() {
        return true;
    }
}
