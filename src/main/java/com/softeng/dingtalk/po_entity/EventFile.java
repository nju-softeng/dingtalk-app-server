package com.softeng.dingtalk.po_entity;

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
public class EventFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileName;

    private String fileType;

    private String fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    EventProperty eventProperty;

    public EventFile(String fileName, String fileId){
        this.fileName=fileName;
        this.fileId=fileId;
    }

    public EventFile(String fileName, String fileId, String fileType, EventProperty eventProperty){
        this.fileName=fileName;
        this.fileId=fileId;
        this.fileType=fileType;
        this.eventProperty = eventProperty;
    }

}
