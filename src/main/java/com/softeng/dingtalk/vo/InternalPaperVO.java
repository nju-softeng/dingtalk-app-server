package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.enums.PaperType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 11:16 AM
 */
@Data
@AllArgsConstructor
public class InternalPaperVO {
    private Integer id;
    private String title;
    private String journal;
    private PaperType paperType;
    private LocalDate issueDate;
    private List<AuthorVO> authors;
}
