package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: zhanyeye
 * @create: 2020-10-08 18:21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalPaperVO {
    private Integer id;
    private String title;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ExternalPaperVO(Integer id, String title, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private String fileName;
    private String fileId;
}
