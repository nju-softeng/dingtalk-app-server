package com.softeng.dingtalk.entity;

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
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private Iteration iteration;
    @ManyToOne
    private User user; //开发者
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private AcRecord acRecord; //获得的ac值
    @Column(columnDefinition="DECIMAL(10,3)")
    private double ac;

    public IterationDetail(Iteration iteration, User user) {
        this.iteration = iteration;
        this.user = user;
    }

}
