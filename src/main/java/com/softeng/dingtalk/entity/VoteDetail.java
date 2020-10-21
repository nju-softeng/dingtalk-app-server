package com.softeng.dingtalk.entity;

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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"internal_vote_id", "user_id"})})
public class VoteDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Boolean result;

    @JsonIgnoreProperties("voteDetails")
    @ManyToOne(fetch = FetchType.LAZY)
    private InternalVote internalVote;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * 投票对应的ac值
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private AcRecord acRecord;

    public VoteDetail(InternalVote internalVote, Boolean result, User user) {
        this.internalVote = internalVote;
        this.result = result;
        this.user = user;
    }

}
