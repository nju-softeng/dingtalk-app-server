package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ProcessProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String conferenceName;

    String year;


    //上传人
    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    /*
    文件类型： Invitation,PPT,PersonalPhoto,ConferencePhoto
     */

    @OneToOne
    @JoinColumn(name = "invitation_file_id")
    ProcessFile invitationFile;

    @OneToOne
    @JoinColumn(name = "ppt_file_id")
    ProcessFile PPTFile;

    @OneToMany
    @JoinColumn(name = "personal_photo_id")
    List<ProcessFile> personalPhotoFileList;

    @OneToMany
    @JoinColumn(name = "conference_photo_id")
    List<ProcessFile> conferencePhotoFileList;

}
