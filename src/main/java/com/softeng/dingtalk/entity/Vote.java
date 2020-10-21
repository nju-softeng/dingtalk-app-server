package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    /**
     * 投票最终结果
     */
    Boolean result;

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
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime createTime;
    /**
     * 投票截止时间
     */
    @JsonFormat
    @Column(nullable = false)
    private LocalDateTime deadline;

    /**
     * 投票对应的论文id
     */
    private int pid;

    @JsonIgnoreProperties("vote")
    @OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE)
    private List<VoteDetail> voteDetails;


    public Vote(LocalDateTime deadline, int pid) {
        this.deadline = deadline;
        this.pid = pid;
    }

    public Vote(int id) {
        this.id = id;
    }


}
