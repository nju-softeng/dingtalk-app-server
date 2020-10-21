package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softeng.dingtalk.enums.PaperType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description 论文实体类
 * @create 2/5/2020 4:33 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Paper {
    /**
     * 论文的投稿结果
     */
    public static final int WAIT = 0;
    public static final int NOTPASS = 1;
    public static final int REVIEWING = 2;
    public static final int REJECT= 3;
    public static final int ACCEPT = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String journal;

    /**
     * 出刊时间
     */
    private LocalDate issueDate;

    /**
     * 论文等级
     */
    @Enumerated(EnumType.STRING)
    private PaperType paperType;

    /**
     * 投稿结果
     */
    private int result;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;

    @JsonIgnoreProperties("paper")
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private InternalVote internalVote;

    @JsonIgnoreProperties("paper")
    @OneToMany(mappedBy = "paper")
    private List<PaperDetail> paperDetails;

    public Paper(int id) {
        this.id = id;
    }

    public Paper(String title, String journal, PaperType paperType, LocalDate issueDate) {
        this.title = title;
        this.journal = journal;
        this.paperType = paperType;
        this.issueDate = issueDate;
    }

    public void update(String title, String journal, PaperType paperType, LocalDate issueDate) {
        this.title = title;
        this.journal = journal;
        this.paperType = paperType;
        this.issueDate = issueDate;
    }


}
