package com.softeng.dingtalk.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description
 * @create 3/14/2020 12:14 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "iteration_detail")
public class IterationDetailPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnoreProperties("iterationDetails")
    @ManyToOne(fetch = FetchType.LAZY)
    private IterationPo iteration;
    /**
     * 开发者
     */
    @ManyToOne
    private UserPo user;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    /**
     * 获得的ac值
     */
    private AcRecordPo acRecordPO;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double ac;

    public IterationDetailPo(IterationPo iteration, UserPo user) {
        this.iteration = iteration;
        this.user = user;
    }

}
