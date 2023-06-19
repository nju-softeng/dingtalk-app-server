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
@Table(name = "event_property")
public class EventProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String name;
    String year;
    String type;
    String path;
    @JoinColumn(name="picture_event_id")
    @OneToMany(cascade = CascadeType.REMOVE)
    List<EventFile> pictureFileList;
    @JoinColumn(name="video_event_id")
    @OneToMany(cascade = CascadeType.REMOVE)
    List<EventFile> videoFileList;
    @JoinColumn(name="doc_event_id")
    @OneToMany(cascade = CascadeType.REMOVE)
    List<EventFile> docFileList;

    public void update(String name, String year, String type) {
        this.name = name;
        this.year = year;
        this.type = type;
    }
}
