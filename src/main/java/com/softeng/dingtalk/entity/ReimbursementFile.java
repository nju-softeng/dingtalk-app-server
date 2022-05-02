package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ReimbursementFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String description;

    String fileName;

    String fileId;

    @ManyToOne
    @JsonIgnore
    Reimbursement reimbursement;

    public ReimbursementFile(String description, String fileName, String fileId, Reimbursement reimbursement) {
        this.description = description;
        this.fileName = fileName;
        this.fileId = fileId;
        this.reimbursement = reimbursement;
    }
}
