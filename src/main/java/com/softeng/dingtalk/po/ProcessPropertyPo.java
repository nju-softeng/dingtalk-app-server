package com.softeng.dingtalk.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "process_property")
public class ProcessPropertyPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String conferenceName;

    String year;

    String filePath;
    //上传人
    @OneToOne
    @JoinColumn(name = "user_id")
    UserPo user;

    /*
    文件类型： Invitation,PPT,PersonalPhoto,ConferencePhoto
     */

    @OneToOne(cascade={CascadeType.REMOVE,CascadeType.PERSIST})
    @JoinColumn(name = "invitation_file_id")
    ProcessFilePo invitationFile;

    @OneToOne(cascade={CascadeType.REMOVE,CascadeType.PERSIST})
    @JoinColumn(name = "ppt_file_id")
    ProcessFilePo PPTFile;

    @OneToMany(cascade={CascadeType.REMOVE},fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_photo_id")
    List<ProcessFilePo> personalPhotoFileList;

    @OneToMany(cascade={CascadeType.REMOVE},fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_photo_id")
    List<ProcessFilePo> conferencePhotoFileList;

    public ProcessPropertyPo(String conferenceName, String year, String filePath, UserPo user){
        this.conferenceName=conferenceName;
        this.year=year;
        this.filePath=filePath;
        this.user = user;
    }

    public void update(String conferenceName,String year){
        this.conferenceName=conferenceName;
        this.year=year;
    }

}
