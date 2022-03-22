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
    private int id;

    String name;
    String year;
    String type;
    String path;
    @JsonIgnoreProperties("EventProperty")
    @OneToMany
    List<EventFile> pictureFileList;
    @JsonIgnoreProperties("EventProperty")
    @OneToMany
    List<EventFile> videoFileList;
    @JsonIgnoreProperties("EventProperty")
    @OneToMany
    List<EventFile> docFileList;

    public void update(String name, String year, String type) {
        this.name = name;
        this.year = year;
        this.type = type;
    }
}
