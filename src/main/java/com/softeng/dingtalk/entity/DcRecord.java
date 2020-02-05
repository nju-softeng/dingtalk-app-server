package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softeng.dingtalk.vo.ApplingVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 周DC值记录（DC日志）
 * @date 12/5/2019
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class DcRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double dvalue;  // Dedication Value
    private double cvalue;  // Contribution Value
    private double dc;
    private double ac;
    private boolean ischeck;   // 是否被审核
    private int yearmonth;     // 表示申请所属 年、月
    private int week;          // 申请所属周
    private LocalDate weekdate; //所属周的一天
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;
    @Version
    private int version;

    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private User applicant;
    @ManyToOne(fetch = FetchType.LAZY)
    private User auditor;


    public void update(double cvalue, double dc, double ac) {
        this.cvalue = cvalue;
        this.dc = dc;
        this.ac = ac;
        this.ischeck = true;
    }

    /**
     * 测试初始化数据用
     * @param applicant_id, auditor_id, dvalue, yearmonth, week
     * @return
     * @Date 1:53 PM 1/2/2020
     **/
    public DcRecord(int applicant_id, int auditor_id, double dvalue, int yearmonth, int week) {
        this.applicant = new User(applicant_id);
        this.auditor = new User(auditor_id);
        this.dvalue = dvalue;
        this.yearmonth = yearmonth;
        this.week = week;
    }

    /**
     * 用户提交申请，创建一个新的dcRecord
     * @param application
     * @return
     * @Date 3:43 PM 2/3/2020
     **/
    public DcRecord(ApplingVO application, int uid, int yearmonth, int week) {
        this.applicant = new User(uid);
        this.auditor = new User(application.getAuditorid());
        this.dvalue = application.getDvalue();
        this.weekdate = application.getDate();
        this.yearmonth = yearmonth;
        this.week = week;

    }


}
