package com.softeng.dingtalk.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author LiXiaoKang
 * @since 2023-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="NewsResp对象", description="公告响应对象")
public class NewsResp implements Serializable {
    @ApiModelProperty(value = "newsId")
    private int id;

    @ApiModelProperty(value = "公告标题")
    private String title;

    @ApiModelProperty(value = "公告链接")
    private String link;

    @ApiModelProperty(value = "公告发布者id")
    private int authorId;

    @ApiModelProperty(value = "公告发布者名字")
    private String authorName;

    @ApiModelProperty(value = "公告是否显示到首页")
    private int isShown;

    @ApiModelProperty(value = "公告发布时间")
    private LocalDateTime releaseTime;
}
