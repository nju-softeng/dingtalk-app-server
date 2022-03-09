package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.enums.PaperType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 11:16 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalPaperVO {
    private Integer id;
    private String title;
    private String journal;
    private PaperType paperType;
    private LocalDate issueDate;
    private List<AuthorVO> authors;
    private Boolean isStudentFirstAuthor;
    private String firstAuthor;
    private int version;
    private String path;
    private String hash;
    private int flatDecision;
    private String reviewFileName;
    private String fileName;
    private String fileId;
}
