package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 3/8/2020 11:26 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@NamedEntityGraph(name="bug.graph",attributeNodes={@NamedAttributeNode("bugDetails")})
public class Bug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title; // 标题
    private String description; // 描述
    private boolean status; // 状态
    private int reporterid; // 报告人id
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project; // bug 所属项目

    @OneToMany(mappedBy = "bug")
    private List<BugDetail> bugDetails;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;
}
