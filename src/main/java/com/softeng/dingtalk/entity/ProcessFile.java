package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ProcessFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileName;

    /*
    文件类型： Invitation,PPT,PersonalPhoto,ConferencePhoto
     */
    private String fileType;

    private String fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    ProcessProperty processProperty;

    public ProcessFile(String fileName, String fileType, String fileId) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileId = fileId;
    }
}
