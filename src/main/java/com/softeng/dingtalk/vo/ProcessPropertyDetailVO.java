package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.po.ProcessFilePo;
import com.softeng.dingtalk.po.UserPo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProcessPropertyDetailVO {
    private Integer id;
    String conferenceName;
    String year;
    String filePath;
    UserPo userPo;
    ProcessFilePo invitationFile;
    ProcessFilePo PPTFile;
    List<ProcessFilePo> personalPhotoFileList;
    List<ProcessFilePo> conferencePhotoFileList;
}
