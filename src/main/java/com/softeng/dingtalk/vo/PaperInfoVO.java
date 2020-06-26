package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 26/06/2020 4:48 PM
 */
@AllArgsConstructor
@Getter
@Setter
public class PaperInfoVO {
    private int id;
    private String title;
    private String journal;
    private LocalDate issueDate;
    private List<AuthorVO> authors;
}
