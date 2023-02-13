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
@Table(name = "project_property")
public class ProjectPropertyPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String name;

    String path;

    @OneToMany(mappedBy = "projectProperty", fetch = FetchType.EAGER)
    List<ProjectPropertyFilePo> projectPropertyFilePoList;

    public ProjectPropertyPo(String name, String path){
        this.name=name;
        this.path=path;
    }

    public void update(String name,String path){
        this.name=name;
        this.path=path;
    }
}
