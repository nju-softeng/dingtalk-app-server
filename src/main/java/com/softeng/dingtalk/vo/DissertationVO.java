package com.softeng.dingtalk.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DissertationVO {
    Integer id;
    Integer userId;
    int state=0;
    String graduateYear;
    String filePath;
}
