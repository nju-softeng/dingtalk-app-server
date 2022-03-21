package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softeng.dingtalk.enums.PaperType;
import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
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
public class InternalPaper implements Paper {
    /**
     * 论文的投稿结果
     */
    public static final int WAIT = 0;
    public static final int NOTPASS = 1;
    public static final int REVIEWING = 2;
    public static final int REJECT = 3;
    public static final int ACCEPT = 4;
    public static final int FLAT = 5;
    public static final int SUSPEND = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String journal;

    /**
     * 更新时间
     */
    private LocalDate updateDate;

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

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private Vote vote;

    @JsonIgnoreProperties("internalPaper")
    @OneToMany(mappedBy = "internalPaper")
    private List<PaperDetail> paperDetails;

    /**
     * @Description 增加非学生一作、平票及上传相关字段及方法
     * @Author Jerrian Zhao
     * @Data 02/03/2022
     */

    /**
     * 是否为学生一作
     */
    @Column(nullable = false)
    private Boolean isStudentFirstAuthor;

    /**
     * 第一作者
     */
    private String firstAuthor;

    /**
     * 版本
     */
    private int version;
    /**
     * 主题
     */
    private String subject;
    /**
     * 年份
     */
    private String year;

    /**
     * 路径
     */
    private String path;

    /**
     * 哈希
     */
    private String hash;

    /**
     * 平票后作者决定
     * -1为无需决定；0为拒绝，投稿中止；1为接受
     */
    private int flatDecision = -1;


    /**
     *组内评审版本文件名称
     */
    private String reviewFileName;

    /**
     *组内评审版本文件Id
     */
    String reviewFileId;

    /**
     *提交版本文件名称
     */
    String submissionFileName;

    /**
     *提交版本文件Id
     */
    String submissionFileId;

    /**
     *发表版本文件名称
     */
    String publishedFileName;

    /**
     *发表版本文件Id
     */
    String publishedFileId;

    /**
     *发表版本Latex文件名称
     */
    String publishedLatexFileName;

    /**
     *发表版本Latex文件Id
     */
    String publishedLatexFileId;
    /**
     *对外版本文件名称
     */
    String publicFileName;
    /**
     *对外版本文件Id
     */
    String publicFileId;
    /**
     *源文件名称
     */
    String sourceFileName;
    /**
     *源文件Id
     */
    String sourceFileId;
    /**
     *评审文件名称
     */
    String commentFileName;
    /**
     *评审文件Id
     */
    String commentFileId;

    public InternalPaper(int id) {
        this.id = id;
    }

    public InternalPaper(String title, String journal, PaperType paperType, LocalDate updateDate, Boolean isStudentFirstAuthor, String firstAuthor) {
        this.title = title;
        this.journal = journal;
        this.paperType = paperType;
        this.updateDate = updateDate;
        this.isStudentFirstAuthor = isStudentFirstAuthor;
        this.firstAuthor = firstAuthor;
    }

    public void update(String title, String journal, PaperType paperType, LocalDate issueDate, String firstAuthor) {
        this.title = title;
        this.journal = journal;
        this.paperType = paperType;
        this.updateDate = issueDate;
        this.firstAuthor = firstAuthor;
    }

    @Override
    public boolean isExternal() {
        return false;
    }

    public boolean hasAccepted() {
        return result == ACCEPT;
    }

    public boolean hasRejected() {
        return result == REJECT;
    }

    public void increaseVersion() {
        this.version += 1;
    }

    public void updatePath(String path) {
        this.path = path;
    }

    public void updateHash(String hash) {
        this.hash = hash;
    }
}
