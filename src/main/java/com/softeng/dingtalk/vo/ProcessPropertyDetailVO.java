package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.po_entity.ProcessFile;
import com.softeng.dingtalk.po_entity.User;
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
    User user;
    ProcessFile invitationFile;
    ProcessFile PPTFile;
    List<ProcessFile> personalPhotoFileList;
    List<ProcessFile> conferencePhotoFileList;
}
