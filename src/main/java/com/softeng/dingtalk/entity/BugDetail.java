package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description
 * @create 3/20/2020 8:49 AM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class BugDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnoreProperties("bugDetails")
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private Bug bug;
    @ManyToOne
    private User user;
    private boolean principal; // 是否为主要责任人
    private double ac;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private AcRecord acRecord;

    public BugDetail(Bug bug, User user, boolean principal, double ac, AcRecord acRecord) {
        this.bug = bug;
        this.user = user;
        this.principal = principal;
        this.ac = ac;
        this.acRecord = acRecord;
    }
}
