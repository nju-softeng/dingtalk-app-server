package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.PaperDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 11:16 AM
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaperVO {
    private Integer id;
    private String title;
    private String journal;
    private int level;
    private LocalDate issueDate;
    private List<PaperDetail> paperDetails;
}
