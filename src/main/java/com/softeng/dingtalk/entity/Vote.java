package com.softeng.dingtalk.entity;

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
    private boolean result;   //投票最终结果
    private int accept;
    private int total;
    private boolean status;   //投票是否截止
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDate startTime;
    private LocalTime endTime;

    @JsonIgnoreProperties("vote")
    @OneToMany(mappedBy = "vote")
    private List<VoteDetail> voteDetails;

    public Vote(LocalTime endTime) {
        this.endTime = endTime;
    }

}
