package com.softeng.dingtalk.po_entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author LiXiaoKang
 * @description 用户组实体类
 * @date 02/02/2023
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 用户研究组名
     */
    private String name;
}
