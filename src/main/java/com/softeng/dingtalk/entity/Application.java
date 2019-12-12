package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 每周绩效申请
 * @date 12/5/2019
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int month;
    private int week;
    private int dc;
    private boolean isCheck;   // 是否已审核
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;   //插入时间
    @Column(unique = true)
    private String flag;

    @ManyToOne
    private User applicant;    // 申请人
    @ManyToOne
    private User auditor;      // 审核人

    @OneToMany(mappedBy = "application")
    private List<AcItem> acItems;  //本次绩效申请包含的 AC申请

    @OneToOne(mappedBy = "application")
    private DcRecord dcRecord;
}
