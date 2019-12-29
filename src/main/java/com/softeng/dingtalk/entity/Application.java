package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 每周绩效申请 （普通用户提交给审核人的申请信息）
 * @date 12/5/2019
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double dc;
    private boolean ischeck;   // 是否已审核

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime insertTime;   //插入时间
    private int week;

    @JsonIgnore
    //todo 重新设计
    //@Column(unique = true)
    private String flag;

    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private User applicant;    // 申请人
    @ManyToOne(fetch = FetchType.LAZY)
    private User auditor;      // 审核人

    @JsonIgnore
    @OneToMany(mappedBy = "application", cascade = CascadeType.REMOVE)
    private List<AcItem> acItems;  //本次绩效申请包含的 AC申请

    @JsonIgnore
    @OneToOne(mappedBy = "application")
    private DcRecord dcRecord;

    public Application(double dc, LocalDateTime localDateTime, int week, User applicant, User auditor) {
        this.insertTime = localDateTime;
        this.week = week;
        this.dc = dc;
        this.applicant = applicant;
        this.auditor = auditor;
    }
    public Application(int id) {
        this.id = id;
    }
}
