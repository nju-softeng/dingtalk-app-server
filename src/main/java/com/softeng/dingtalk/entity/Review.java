package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description 论文评审意见表
 * @create 6/4/2020 12:13 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private User user;

    /**
     * 评审意见所对应的论文id
     */
    private int paperid;

    /**
     * 是否对应外部论文
     */
    private boolean external;

    /**
     * 评审意见markdown
     */
    @Column(columnDefinition="TEXT")
    private String md;
    @Column(columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime updateTime;

}
