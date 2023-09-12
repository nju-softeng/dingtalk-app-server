package com.softeng.dingtalk.dto.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PatentReq对象", description="专利申请请求对象")
public class PatentReq implements Serializable {
    private int id;
    private String name;
    private String type;
    private String year;
    private String obligee;
    private Integer applicantId;
    private List<Integer> inventorsIdList;
    private String version;
    private String filePath;
    private String patentFileName;
    private String patentFileId;
    private String handlingFileName;
    private String handlingFileId;
    private String authorizationFileName;
    private String authorizationFileId;
    private int state;
}
