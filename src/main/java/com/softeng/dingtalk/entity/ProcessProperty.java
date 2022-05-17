package com.softeng.dingtalk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;

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

    String filePath;
    //上传人
    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    /*
    文件类型： Invitation,PPT,PersonalPhoto,ConferencePhoto
     */

    @OneToOne(cascade={CascadeType.REMOVE,CascadeType.PERSIST})
    @JoinColumn(name = "invitation_file_id")
    ProcessFile invitationFile;

    @OneToOne(cascade={CascadeType.REMOVE,CascadeType.PERSIST})
    @JoinColumn(name = "ppt_file_id")
    ProcessFile PPTFile;

    @OneToMany(cascade={CascadeType.REMOVE},fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_photo_id")
    List<ProcessFile> personalPhotoFileList;

    @OneToMany(cascade={CascadeType.REMOVE},fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_photo_id")
    List<ProcessFile> conferencePhotoFileList;

    public ProcessProperty(String conferenceName,String year,String filePath,User user){
        this.conferenceName=conferenceName;
        this.year=year;
        this.filePath=filePath;
        this.user=user;
    }

    public void update(String conferenceName,String year){
        this.conferenceName=conferenceName;
        this.year=year;
    }

}
