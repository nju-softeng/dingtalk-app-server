package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatentVO {
    Integer id;
    String name;
    String type;
    String year;
    String obligee;
    Integer applicantId;
    List<Integer> inventorsIdList;
    String version;
    String filePath;
    String patentFileName;
    String patentFileId;
    String handlingFileName;
    String handlingFileId;
    String authorizationFileName;
    String authorizationFileId;
    int state=0;

}
