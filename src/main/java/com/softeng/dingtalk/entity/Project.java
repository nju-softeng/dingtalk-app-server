package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 2/25/2020 11:52 AM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@NamedEntityGraph(name="project.graph",attributeNodes={@NamedAttributeNode("projectDetails")})
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY) //设置many端对one端延时加载，仅需要其ID
    private User auditor;
    private int num;
    @Column(columnDefinition="DECIMAL(10,3)")
    private double expectedAC;
    private boolean status;
    private LocalDate beginTime;
    private LocalDate endTime;
    private LocalDate finishTime;
    @JsonIgnoreProperties("project")
    @OneToMany(mappedBy = "project")
    private List<ProjectDetail> projectDetails;

    public Project(String name, List<ProjectDetail> projectDetails) {
        this.name = name;
        this.projectDetails = projectDetails;
    }

    public Project(String name, User auditor, LocalDate beginTime, LocalDate endTime) {
        this.name = name;
        this.auditor = auditor;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

}
