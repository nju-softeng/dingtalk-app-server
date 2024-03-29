package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class PatentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int num;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Patent patent;
    @ManyToOne
    private User user;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<AcRecord> acRecordList;

    public PatentDetail(int num, Patent patent, User user) {
        this.num = num;
        this.patent = patent;
        this.user = user;
    }
}
