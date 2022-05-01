package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.User;
import lombok.Data;

@Data
public class ReimbursementVO {
    private Integer id;
    int state=-1;
    String name;
    String type;
    String path;
}
