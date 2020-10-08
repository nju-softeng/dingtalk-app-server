package com.softeng.dingtalk.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: zhanyeye
 * @create: 2020-10-08 18:21
 **/
@Data
public class ExternalVoteVO {
    private Integer id;
    private String title;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
