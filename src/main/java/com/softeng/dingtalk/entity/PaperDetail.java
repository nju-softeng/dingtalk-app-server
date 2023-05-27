package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description 论文
 * @create 2/5/2020 4:51 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "paper_detail")
public class PaperDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int num;
    @Column(columnDefinition="DECIMAL(10,3)")
    @Deprecated
    private double ac;
    @JsonIgnoreProperties("paperDetails")
    @ManyToOne(fetch = FetchType.LAZY)
    private InternalPaper internalPaper;
    @ManyToOne
    private User user;
    @OneToOne(cascade = CascadeType.REMOVE)
    private AcRecord acRecord;

    public PaperDetail(InternalPaper internalPaper, User user, int num) {
        this.internalPaper = internalPaper;
        this.user = user;
        this.num = num;
    }

    public PaperDetail(InternalPaper internalPaper, int uid, int num) {
        this.internalPaper = internalPaper;
        this.user = new User(uid);
        this.num = num;
    }

}
