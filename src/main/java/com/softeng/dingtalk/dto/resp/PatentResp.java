package com.softeng.dingtalk.dto.resp;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PatentResp对象", description="专利申请响应对象")
public class PatentResp implements Serializable {
    private int id;

    //专利名字
    private String name;

    //权利人
    private String obligee;

    private UserResp applicant;

    //发明人
    private List<PatentDetailResp> patentDetailList;

    //年份
    private String year;
    //类型
    private String type;

    //版本
    private String version;

    //文件目录
    private String filePath;

    //专利内审文件名字
    private String reviewFileName;

    //专利内审文件id
    private String reviewFileId;

    //专利提交文件名字
    private String submissionFileName;

    //专利提交文件id
    private String submissionFileId;

    //专利评论文件名字
    private String commentFileName;

    //专利评论文件id
    private String commentFileId;

    //受理文件名
    private String handlingFileName;

    //受理文件id
    private String handlingFileId;

    //授权文件名
    private String authorizationFileName;

    //授权文件id
    private String authorizationFileId;

    //状态： 0待内审，1内审不通过，2内审通过，3专利授权，4专利驳回
    private int state;
}
