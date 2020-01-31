package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description 每周绩效申请中的AC值申请  （一个 DcRecord 可能有多个 acItem: 一个申请可能包含多个ac申请）
 * @date 12/5/2019
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
public class AcItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double ac;
    private String reason;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private DcRecord dcRecord;  //ac申请属于的周绩效申请
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private AcRecord acRecord;
}
