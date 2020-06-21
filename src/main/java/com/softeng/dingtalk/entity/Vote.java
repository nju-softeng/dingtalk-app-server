package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softeng.dingtalk.vo.VoteVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 2/5/2020 5:14 PM
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * 投票最终结果
     */
    private Boolean result;
    /**
     * 支持人数
     */
    private int accept;
    /**
     * 总投票人数
     */
    private int total;
    /**
     * 投票是否截止
     */
    private boolean status;
    /**
     * 投票截止时间
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

}
