package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.PaperDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private int id;
    private String title;
    private String journal;
    private int level;
    private List<PaperDetail> paperDetails;
}
