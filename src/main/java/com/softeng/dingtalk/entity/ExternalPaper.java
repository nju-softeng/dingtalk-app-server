package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @description:
 * @author: zhanyeye
 * @create: 2020-10-21 20:07
 **/
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ExternalPaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private Vote vote;

    public ExternalPaper(String title) {
        this.title = title;
    }
}
