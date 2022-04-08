package com.softeng.dingtalk.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class EventProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String name;
    String year;
    String type;
    String path;
    @JsonIgnoreProperties("EventProperty")
    @OneToMany(mappedBy = "eventProperty", cascade = CascadeType.REMOVE)
    List<EventFile> pictureFileList;
    @JsonIgnoreProperties("EventProperty")
    @OneToMany(mappedBy = "eventProperty", cascade = CascadeType.REMOVE)
    List<EventFile> videoFileList;
    @JsonIgnoreProperties("EventProperty")
    @OneToMany(mappedBy = "eventProperty", cascade = CascadeType.REMOVE)
    List<EventFile> docFileList;

    public void update(String name, String year, String type) {
        this.name = name;
        this.year = year;
        this.type = type;
    }
}
