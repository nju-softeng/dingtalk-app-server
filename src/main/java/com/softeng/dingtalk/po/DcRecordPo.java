package com.softeng.dingtalk.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softeng.dingtalk.vo.ApplyVO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author zhanyeye
 * @description 周DC值记录（DC日志）
 * @date 12/5/2019
 */
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "dc_record")
@NamedEntityGraph(name="dcRecord.graph",attributeNodes={@NamedAttributeNode("acItems"), @NamedAttributeNode("auditor")})
public class DcRecordPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Dedication Value
     */
    @Column(columnDefinition="DECIMAL(10,3)")
    private double dvalue;
    /**
     * Contribution Value
     */
    @Column(columnDefinition="DECIMAL(10,3)")
    private double cvalue;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double dc;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double ac;

    /**
     * 是否被审核
     */
    private boolean status;
    /**
     * 表示申请所属 年、月
     */
    @Deprecated
    private int yearmonth;
    /**
     * 申请所属周
     */
    @Deprecated
    private int week;
    /**
     * 所属周的一天
     */
    @Deprecated
    private LocalDate weekdate;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;
    @JsonIgnore
    @Version
    private int version;
    @JsonIgnore
    @Deprecated
    private int dateCode;

    /**
     * 设置many端对one端延时加载，仅需要其ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private UserPo applicant;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserPo auditor;

    @JsonIgnoreProperties("dcRecord")
    @OneToMany(mappedBy = "dcRecord")
    @Deprecated
    private List<AcItemPo> acItems;



    public void update(double cvalue, double dc, double ac) {
        this.cvalue = cvalue;
        this.dc = dc;
        this.ac = ac;
        this.status = true;

    }

    public DcRecordPo reApply(int authorid, double dvalue, LocalDate weekdate, int dateCode) {
        this.auditor = new UserPo(authorid);
        this.dvalue = dvalue;
        this.weekdate = weekdate;
        this.status = false;
        this.yearmonth = dateCode / 10;
        this.week = dateCode % 10;
        this.dateCode = dateCode;
        return this;
    }

    public DcRecordPo(int uid, ApplyVO vo, int dateCode) {
        this.applicant = new UserPo(uid);
        this.auditor = new UserPo(vo.getAuditorid());
        this.dvalue = vo.getDvalue();
        this.ac = vo.getAc();
        this.weekdate = vo.getDate();
        this.dateCode = dateCode;
        this.yearmonth = dateCode / 10;
        this.week = dateCode % 10;
    }


    /**
     * 审核人创建新的申请时用该方法更新
     * @param uid
     * @param vo
     * @param dateCode
     */
    public DcRecordPo setByAuditor(int uid, ApplyVO vo, int dateCode) {
        this.applicant = new UserPo(uid);
        this.auditor = new UserPo(vo.getAuditorid());
        this.dvalue = vo.getDvalue();
        this.cvalue = vo.getCvalue();
        this.dc = vo.getDvalue() * vo.getCvalue();
        this.weekdate = vo.getDate();
        this.dateCode = dateCode;
        this.yearmonth = dateCode / 10;
        this.week = dateCode % 10;
        this.ac = 0;
        this.status = true;
        Optional.ofNullable(vo.getAcItemPos()).orElse(new ArrayList<>())
                .forEach(acItem -> {
                    this.ac += acItem.getAc();
                });
        return this;
    }



}
