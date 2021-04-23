package com.softeng.pms.entity;

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
public class IterationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnoreProperties("iterationDetails")
    @ManyToOne(fetch = FetchType.LAZY)
    private Iteration iteration;
    /**
     * 开发者
     */
    @ManyToOne
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    /**
     * 获得的ac值
     */
    private AcRecord acRecord;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double ac;

    public IterationDetail(Iteration iteration, User user) {
        this.iteration = iteration;
        this.user = user;
    }

}
