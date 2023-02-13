package com.softeng.dingtalk.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "patent_detail")
public class PatentDetailPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int num;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private PatentPo patent;
    @ManyToOne
    private UserPo user;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<AcRecordPo> acRecordList;

    public PatentDetailPo(int num, PatentPo patent, UserPo user) {
        this.num = num;
        this.patent = patent;
        this.user = user;
    }
}
