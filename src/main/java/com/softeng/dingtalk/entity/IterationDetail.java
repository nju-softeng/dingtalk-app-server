package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author zhanyeye
 * @description
 * @create 3/14/2020 12:14 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class IterationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
