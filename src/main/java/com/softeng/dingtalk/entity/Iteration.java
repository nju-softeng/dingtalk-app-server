package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author zhanyeye
 * @description
 * @create 3/14/2020 11:21 AM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Iteration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private boolean status;
    private LocalDate beginTime;
    private LocalDate endTime;
    private LocalDate finishTime;

    @ManyToOne
    private Project project;


}
