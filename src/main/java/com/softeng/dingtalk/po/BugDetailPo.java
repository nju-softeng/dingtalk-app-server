package com.softeng.dingtalk.po;

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
@Table(name = "bug_detail")
public class BugDetailPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnoreProperties("bugDetails")
    @ManyToOne(fetch = FetchType.LAZY)
    private BugPo bug;
    @ManyToOne
    private UserPo user;
    /**
     * 是否为主要责任人
     */
    private boolean principal;
    private double ac;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private AcRecordPo acRecord;

    public BugDetailPo(BugPo bug, UserPo user, boolean principal, double ac, AcRecordPo acRecord) {
        this.bug = bug;
        this.user = user;
        this.principal = principal;
        this.ac = ac;
        this.acRecord = acRecord;
    }
}
