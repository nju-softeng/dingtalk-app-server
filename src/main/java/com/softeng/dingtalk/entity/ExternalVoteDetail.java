package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @description: 外部评审投票的详情
 * @author: zhanyeye
 * @create: 2020-10-06 16:23
 **/
@Getter
@Setter
@Entity
@NoArgsConstructor
public class ExternalVoteDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Boolean result;

    /**
     * 对应的投票
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private ExternalVote externalVote;

    /**
     * 投票者id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * 投票对应的ac值
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private AcRecord acRecord;

}
