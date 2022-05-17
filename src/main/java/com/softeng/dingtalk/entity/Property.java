package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @Description 实验室固定资产项实体类
 * @Author Jerrian Zhao
 * @Data 01/24/2022
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnoreProperties("allPrizes")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * 物品名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 物品型号
     */
    @Column(nullable = false)
    private String type;

    /**
     * 开始时间
     */
    @Column(nullable = false)
    private LocalDate startTime;

    /**
     * 保管人
     */
    private String preserver;
    /**
     * 备注
     */
    private String remark;
    /**
     * 软删除标识
     */
    @Column(nullable = false)
    private boolean deleted = false;

    public Property(User user, String name, String type, String preserver, String remark,LocalDate startTime) {
        this.user = user;
        this.name = name;
        this.type = type;
        this.preserver = preserver;
        this.remark = remark;
        this.startTime = startTime;
    }
}
