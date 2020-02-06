package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description
 * @create 2/5/2020 5:27 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class VoteDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean result;

    @JsonIgnoreProperties("voteDetails")
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private Vote vote;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
