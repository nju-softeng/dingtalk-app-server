package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class DissertationProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String year;
    String name;
    String PDFFileName;
    String PDFFileId;
    String LatexFileName;
    String LatexFileId;
}
