package com.softeng.dingtalk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhanyeye
 * @description
 * @create 2/26/2020 2:17 PM
 */
@Entity
@Table(name="view_ac")
public class ViewAc {
    @Id
    @Column(name="id")
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="total")
    private int total;
}
