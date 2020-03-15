package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 3/14/2020 11:21 AM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@NamedEntityGraph(name="iteration.graph",attributeNodes={@NamedAttributeNode("iterationDetails")})
public class Iteration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int cnt; // 第几次迭代
    private boolean status;
    private LocalDate beginTime;
    private LocalDate endTime;
    private LocalDate finishTime;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double expectedAC;
    private int prevIteration; // 上一个迭代版本id


    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private User auditor;
    @ManyToOne
    private Project project;

    @JsonIgnoreProperties("iteration")
    @OneToMany(mappedBy = "iteration", cascade = CascadeType.REMOVE)
    private List<IterationDetail> iterationDetails;

    public Iteration(int id) {
        this.id = id;
    }

    public Iteration(int cnt, User auditor, LocalDate beginTime, LocalDate endTime) {
        this.cnt = cnt;
        this.auditor = auditor;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }


}
