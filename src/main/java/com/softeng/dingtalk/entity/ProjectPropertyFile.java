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
public class ProjectPropertyFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String version;
    @ManyToOne
    @JsonIgnore
    ProjectProperty projectProperty;

    String codeFileName;
    String codeFileId;
    String reportFileName;
    String reportFileId;
}
