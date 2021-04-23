package com.softeng.pms.vo;

import com.softeng.pms.enums.PaperType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 26/06/2020 4:48 PM
 */
@Data
public class PaperInfoVO {
    private int id;
    private String title;
    private String journal;
    private LocalDate issueDate;
    private int result;
    private PaperType paperType;
    private Boolean v_status;
    private Boolean v_result;
    private List<AuthorVO> authors;
}
