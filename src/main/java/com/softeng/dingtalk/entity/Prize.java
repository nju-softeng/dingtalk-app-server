package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @Description 获奖奖项实体类
 * @Author Jerrian Zhao
 * @Data 01/24/2022
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Prize {
    /**
     * 表示奖项级别的静态常量
     */
    public static final int SCHOOL_LEVEL = 0;
    public static final int PROVINCE_LEVEL = 1;
    public static final int NATIONAL_LEVEL = 2;
    public static final int INTERNATIONAL_LEVEL = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnoreProperties("allPrizes")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * 获奖时间
     */
    @Column(nullable = false)
    private LocalDate prizeTime;

    /**
     * 奖项名称
     */
    @Column(nullable = false)
    private String prizeName;

    /**
     * 奖项级别
     */
    @Column(nullable = false)
    private int level;

    /**
     * 备注
     */
    private String remark;

    /**
     * 软删除标识
     */
    @Column(nullable = false)
    private boolean deleted = false;

    public Prize(User user, LocalDate prizeTime, String prizeName, int level, String remark) {
        this.user = user;
        this.prizeTime = prizeTime;
        this.prizeName = prizeName;
        this.level = level;
        this.remark = remark;
    }

    public String getPrizeLevelName(int level){
        switch (level){
            case 0:
                return "校级";
            case 1:
                return "省级";
            case 2:
                return "国家级";
            case 3:
                return "国际级";
            default:
                return "暂无级别";
        }
    }
}
