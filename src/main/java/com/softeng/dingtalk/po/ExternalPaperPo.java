package com.softeng.dingtalk.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: zhanyeye
 * @create: 2020-10-21 20:07
 **/
@Entity
@Data
@NoArgsConstructor
@Deprecated
@Table(name = "external_paper")
public class ExternalPaperPo implements PaperPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * 论文标题
     */
    private String title;
    /**
     * 该外部论文的最终录用结果, -1为还没有结果, 0为拒绝, 1为接受, 2为中止
     */
    private int result = -1;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private VotePo vote;

    /**
     * 更新时间
     */
    private LocalDate updateDate;

    /**
     * 插入时间
     */
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime insertTime;
    /**
     * 主题
     */
    private String theme;

    /**
     * 路径
     */
    private String path;

    /**
     * 哈希
     */
    private String hash;

    /**
     *评审版本文件名称
     */
    private String reviewFileName;

    /**
     *评审版本文件Id
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

    public ExternalPaperPo(String title) {
        this.title = title;
    }

    @Override
    public boolean isExternal() {
        return true;
    }
}
