package com.softeng.dingtalk.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 内部论文评审投票
 * @create 2/5/2020 5:14 PM
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "vote")
public class VotePo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    /**
     * 投票最终结果, -1为未结束, 0为拒绝, 1为接受, 2为平票
     */
    int result = -1;

    /**
     * 平票后作者是否选择外投
     */
    Boolean authorTendency;

    /**
     * 支持人数
     */
    int accept;

    /**
     * 总投票人数
     */
    int total;

    /**
     * 投票是否截止
     */
    boolean status;

    /**
     * 投票开始时间
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * 投票截止时间
     */
    @Column(nullable = false)
    private LocalDateTime endTime;

    /**
     * 投票对应的论文id
     */
    private int pid;
    /**
     * 是否是外部评审投票
     */
    private boolean external;
    /**
     * 外部评审投票是否已经开始
     */
    private boolean started;

    @JsonIgnoreProperties("vote")
    @OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE)
    private List<VoteDetailPo> voteDetailPos;


    public VotePo(LocalDateTime startTime , LocalDateTime endTime, int pid) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.pid = pid;
    }

    public VotePo(LocalDateTime startTime, LocalDateTime endTime, boolean external, int pid) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.external = external;
        this.pid = pid;
    }

    public VotePo(int id) {
        this.id = id;
    }


}
