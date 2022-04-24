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
public class ProjectProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String name;

    String path;

    @OneToMany(mappedBy = "projectProperty")
    List<ProjectPropertyFile> projectPropertyFileList;

    public ProjectProperty(String name,String path){
        this.name=name;
        this.path=path;
    }

    public void update(String name,String path){
        this.name=name;
        this.path=path;
    }
}
