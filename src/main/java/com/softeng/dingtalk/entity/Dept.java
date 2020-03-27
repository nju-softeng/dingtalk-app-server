package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author zhanyeye
 * @description 部门
 * @create 3/27/2020 9:05 AM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Dept {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "deptid")
    @GenericGenerator(name = "deptid", strategy = "com.softeng.dingtalk.entity.CustomIDGenerator")
    private Long id;
    private String name;
    private Long parentid;

    public Dept(Long id, String name, Long parentid) {
        this.id = id;
        this.name = name;
        this.parentid = parentid;
    }

}
