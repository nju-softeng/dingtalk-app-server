package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class PaperDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int num;
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private Paper paper;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private AcRecord acRecord;

}
