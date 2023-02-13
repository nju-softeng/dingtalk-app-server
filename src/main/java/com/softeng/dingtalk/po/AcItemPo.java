package com.softeng.dingtalk.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description 每周绩效申请中的AC值申请  （一个 DcRecord 可能有多个 acItem: 一个申请可能包含多个ac申请）
 * @date 12/5/2019
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
@Deprecated
@Table(name = "ac_item")
public class AcItemPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double ac;
    private String reason;
    private boolean status;

    /**
     * ac申请属于的周绩效申请
     * 设置many端对one端延时加载，仅需要其ID
     */
    @JsonIgnore
    @JsonIgnoreProperties("acItems")
    @ManyToOne(fetch = FetchType.LAZY)
    private DcRecordPo dcRecord;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private AcRecordPo acRecord;

    public AcItemPo(String reason, double ac) {
        this.reason = reason;
        this.ac = ac;
    }
}
