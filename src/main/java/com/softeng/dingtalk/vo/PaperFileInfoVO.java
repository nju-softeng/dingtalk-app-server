package com.softeng.dingtalk.vo;

import lombok.Data;

@Data
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
}
