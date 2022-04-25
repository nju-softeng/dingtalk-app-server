package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Reimbursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    //审核状态： 0 审核中，1 审核通过， 2 审核不通过
    int state;

    // 差旅报销，国内会议报销，国际会议报销，办公用品报销
    String type;

    String path;

    @OneToMany(mappedBy = "reimbursement")
    List<ReimbursementFile> reimbursementFileList;
}
