package com.softeng.dingtalk.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "reimbursement")
public class ReimbursementPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    UserPo user;

    //审核状态： -1 未审核， 0 审核中，1 审核通过， 2 审核不通过
    int state=-1;
    String name;
    // 差旅报销，国内会议报销，国际会议报销，办公用品报销
    String type;

    String path;

    @OneToMany(mappedBy = "reimbursementPo",cascade = {CascadeType.REMOVE},fetch = FetchType.EAGER)
    List<ReimbursementFilePo> reimbursementFilePoList;

    public ReimbursementPo(String name, String type, String path) {
        this.name=name;
        this.type = type;
        this.path = path;
    }

    public void update(String name,String type, String path) {
        this.name=name;
        this.type = type;
        this.path = path;
    }
}
