package com.softeng.dingtalk.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "iteration")
@NamedEntityGraph(name="iteration.graph",attributeNodes={@NamedAttributeNode("iterationDetailPos")})
public class IterationPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * 第几次迭代
     */
    private int cnt;
    private boolean status;
    private LocalDate beginTime;
    private LocalDate endTime;
    private LocalDate finishTime;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double expectedAC;
    /**
     * 上一个迭代版本id
     */
    private int prevIteration;
    /**
     * 持续成功次数
     */
    private int conSuccess;


    @ManyToOne(fetch = FetchType.LAZY)
    private UserPo auditor;
    @ManyToOne
    private ProjectPo project;

    @JsonIgnoreProperties("iteration")
    @OneToMany(mappedBy = "iteration", cascade = CascadeType.REMOVE)
    private List<IterationDetailPo> iterationDetailPos;

    public IterationPo(int id) {
        this.id = id;
    }

    public IterationPo(int cnt, UserPo auditor, LocalDate beginTime, LocalDate endTime) {
        this.cnt = cnt;
        this.auditor = auditor;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }


}
