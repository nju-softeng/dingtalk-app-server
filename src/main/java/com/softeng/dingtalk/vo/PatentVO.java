package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatentVO {
    private Integer id;
    private String name;
    private Integer obligeeId;
    private List<Integer> inventorsId;
    String handlingFilePath;
    String authorizationFilePath;
    int status=0;

}
