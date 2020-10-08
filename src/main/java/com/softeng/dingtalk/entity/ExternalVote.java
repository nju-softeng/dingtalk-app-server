package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @description: 外部评审投票
 * @author: zhanyeye
 * @create: 2020-10-06 16:11
 **/
@Getter
@Setter
@Entity
@NoArgsConstructor
public class ExternalVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    /**
     * 投票最终结果
     */
    private Boolean result;
    /**
     * 支持人数
     */
    private int accept;
    /**
     * 总投票人数
     */
    private int total;
    /**
     * 投票是否截止
     */
    private boolean status;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

}
