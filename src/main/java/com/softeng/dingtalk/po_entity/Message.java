package com.softeng.dingtalk.po_entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author zhanyeye
 * @description
 * @create 2/6/2020 1:56 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;
    private boolean status;
    /**
     * 接收人id
     */
    private int uid;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime createTime;

    public Message(String title, String content, int uid) {
        this.title = title;
        this.content = content;
        this.uid = uid;
    }

}
