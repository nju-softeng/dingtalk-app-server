package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaperFileInfoVO {
    int id;
    String reviewFileName;
    String reviewFileId;
    String submissionFileName;
    String submissionFileId;
    String publishedFileName;
    String publishedFileId;
    String publishedLatexFileName;
    String publishedLatexFileId;
    String publicFileName;
    String publicFileId;
    String sourceFileName;
    String sourceFileId;
    String commentFileName;
    String commentFileId;
    public PaperFileInfoVO(int id, String reviewFileName, String reviewFileId, String submissionFileName, String submissionFileId, String publishedFileName, String publishedFileId, String publishedLatexFileName, String publishedLatexFileId) {
        this.id = id;
        this.reviewFileName = reviewFileName;
        this.reviewFileId = reviewFileId;
        this.submissionFileName = submissionFileName;
        this.submissionFileId = submissionFileId;
        this.publishedFileName = publishedFileName;
        this.publishedFileId = publishedFileId;
        this.publishedLatexFileName = publishedLatexFileName;
        this.publishedLatexFileId = publishedLatexFileId;
    }

}
