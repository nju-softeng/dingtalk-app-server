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
@Table(name = "event_property")
public class EventPropertyPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String name;
    String year;
    String type;
    String path;
    @JoinColumn(name="picture_event_id")
    @OneToMany(cascade = CascadeType.REMOVE)
    List<EventFilePo> pictureFileList;
    @JoinColumn(name="video_event_id")
    @OneToMany(cascade = CascadeType.REMOVE)
    List<EventFilePo> videoFileList;
    @JoinColumn(name="doc_event_id")
    @OneToMany(cascade = CascadeType.REMOVE)
    List<EventFilePo> docFileList;

    public void update(String name, String year, String type) {
        this.name = name;
        this.year = year;
        this.type = type;
    }
}
