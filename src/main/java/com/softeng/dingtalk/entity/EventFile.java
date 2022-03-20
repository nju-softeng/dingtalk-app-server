package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class EventFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int eventId;

    private String fileName;

    private String fileId;

    @JsonIgnoreProperties({"pictureFileList","videoFileList","docFileList"})
    @ManyToOne
    EventProperty eventProperty;

}
