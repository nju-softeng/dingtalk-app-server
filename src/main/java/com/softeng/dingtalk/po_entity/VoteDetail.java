package com.softeng.dingtalk.po_entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description
 * @create 2/5/2020 5:27 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@NamedEntityGraph(name="voteDetail.graph",attributeNodes={@NamedAttributeNode("acRecord")})
//@Table(name = "vote_detail")
@Table(name = "vote_detail",uniqueConstraints = {@UniqueConstraint(columnNames={"vote_id", "user_id"})})
public class VoteDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private boolean result;

    @JsonIgnoreProperties("voteDetails")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 投票对应的ac值
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private AcRecord acRecord;

    public VoteDetail(Vote vote, boolean result, User user) {
        this.vote = vote;
        this.result = result;
        this.user = user;
    }

}
