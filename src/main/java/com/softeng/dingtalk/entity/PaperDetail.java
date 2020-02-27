package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class PaperDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int num;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double ac;
    @JsonIgnoreProperties("paperDetails")
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private Paper paper;
    @ManyToOne
    private User user;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private AcRecord acRecord;

}
