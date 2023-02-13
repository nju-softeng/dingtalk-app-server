package com.softeng.dingtalk.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "reimbursement_file")
public class ReimbursementFilePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String description;

    String fileName;

    String fileId;

    @ManyToOne
    @JsonIgnore
    ReimbursementPo reimbursementPo;

    public ReimbursementFilePo(String description, String fileName, String fileId, ReimbursementPo reimbursementPo) {
        this.description = description;
        this.fileName = fileName;
        this.fileId = fileId;
        this.reimbursementPo = reimbursementPo;
    }
}
