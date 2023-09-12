package com.softeng.dingtalk.entity;

import com.softeng.dingtalk.enums.Position;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author zhanyeye
 * @description 津贴标准
 * @create 5/27/2020 6:32 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "subsidy_level")
public class SubsidyLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * 学位
     */
    private Position position;

    /**
     * 津贴
     */
    @Column(columnDefinition="DECIMAL(10,3)")
    private double subsidy;

    public SubsidyLevel(Position position, double subsidy) {
        this.position = position;
        this.subsidy = subsidy;
    }


}
