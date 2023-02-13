package com.softeng.dingtalk.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "bug")
@NamedEntityGraph(name="bug.graph",attributeNodes={@NamedAttributeNode("bugDetails")})
public class BugPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 状态
     */
    private Boolean status;
    /**
     * 报告人id
     */
    private int reporterid;
    /**
     * bug 所属项目
     */
    @ManyToOne
    private ProjectPo project;

    @JsonIgnoreProperties("bug")
    @OneToMany(mappedBy = "bug", cascade = CascadeType.REMOVE)
    private List<BugDetailPo> bugDetails;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;

    public BugPo(int id) {
        this.id = id;
    }

}
