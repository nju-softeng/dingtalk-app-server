package com.softeng.dingtalk.vo;
import lombok.Data;

@Data
public class ReimbursementVO {
    private Integer id;
    int state=-1;
    String name;
    String type;
    String path;
}
