package com.softeng.dingtalk.po_entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_property_file")
public class ProjectPropertyFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String version;
    @ManyToOne
    @JsonIgnore
    ProjectProperty projectProperty;

    String codeFileName;
    String codeFileId;
    String reportFileName;
    String reportFileId;

    public void update(String version){
        this.version=version;
    }
}
