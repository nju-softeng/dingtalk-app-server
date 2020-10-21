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
public class InternalVote extends Vote {
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

    @JsonIgnoreProperties("internalVote")
    @OneToMany(mappedBy = "internalVote", cascade = CascadeType.REMOVE)
    private List<VoteDetail> voteDetails;


    public InternalVote(LocalDateTime deadline, int pid) {
        this.deadline = deadline;
        this.pid = pid;
    }

    public InternalVote(int id) {
        this.id = id;
    }


}
