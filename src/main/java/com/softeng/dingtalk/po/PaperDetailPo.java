package com.softeng.dingtalk.po;

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
public class PaperDetailPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int num;
    @Column(columnDefinition="DECIMAL(10,3)")
    @Deprecated
    private double ac;
    @JsonIgnoreProperties("paperDetails")
    @ManyToOne(fetch = FetchType.LAZY)
    private InternalPaperPo internalPaper;
    @ManyToOne
    private UserPo user;
    @OneToOne(cascade = CascadeType.REMOVE)
    private AcRecordPo acRecord;

    public PaperDetailPo(InternalPaperPo internalPaper, UserPo user, int num) {
        this.internalPaper = internalPaper;
        this.user = user;
        this.num = num;
    }

    public PaperDetailPo(InternalPaperPo internalPaper, int uid, int num) {
        this.internalPaper = internalPaper;
        this.user = new UserPo(uid);
        this.num = num;
    }

}
