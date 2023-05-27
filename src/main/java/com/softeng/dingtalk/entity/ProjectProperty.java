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
@Table(name = "project_property")
public class ProjectProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String name;

    String path;

    @OneToMany(mappedBy = "projectProperty", fetch = FetchType.EAGER)
    List<ProjectPropertyFile> projectPropertyFileList;

    public ProjectProperty(String name, String path){
        this.name=name;
        this.path=path;
    }

    public void update(String name,String path){
        this.name=name;
        this.path=path;
    }
}
