package com.softeng.dingtalk.entity;

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
    Reimbursement reimbursement;
}
