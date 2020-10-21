package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: zhanyeye
 * @create: 2020-10-20 21:36
 **/

@Getter
@Setter
@MappedSuperclass
public abstract class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    /**
     * 投票最终结果
     */
    Boolean result;

    /**
     * 支持人数
     */
    int accept;

    /**
     * 总投票人数
     */
    int total;
    /**
     * 投票是否截止
     */
    boolean status;

    LocalDateTime startTime;
    LocalDateTime endTime;

}
