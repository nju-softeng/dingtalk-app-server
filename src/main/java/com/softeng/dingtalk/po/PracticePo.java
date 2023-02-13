package com.softeng.dingtalk.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "practice")
public class PracticePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserPo user;
    //实习单位
    String companyName;
    //实习部门
    String department;
    //开始时间
    LocalDate start;
    //结束时间
    LocalDate end;
    //状态 -1拒绝，0审核中，1通过
    int state=0;

    public PracticePo(UserPo user, String companyName, String department, LocalDate start, LocalDate end, int state) {
        this.user = user;
        this.companyName = companyName;
        this.department = department;
        this.start = start;
        this.end = end;
        this.state = state;
    }

    public void update(String companyName, String department, LocalDate start, LocalDate end, int state) {
        this.companyName = companyName;
        this.department = department;
        this.start = start;
        this.end = end;
        this.state = state;
    }
}
