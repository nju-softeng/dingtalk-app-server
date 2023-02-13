package com.softeng.dingtalk.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "event_file")
public class EventFilePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileName;

    private String fileType;

    private String fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    EventPropertyPo eventPropertyPo;

    public EventFilePo(String fileName, String fileId){
        this.fileName=fileName;
        this.fileId=fileId;
    }

    public EventFilePo(String fileName, String fileId, String fileType, EventPropertyPo eventPropertyPo){
        this.fileName=fileName;
        this.fileId=fileId;
        this.fileType=fileType;
        this.eventPropertyPo = eventPropertyPo;
    }

}
