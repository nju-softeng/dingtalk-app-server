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
    private String fileName;
    private String fileId;
}
