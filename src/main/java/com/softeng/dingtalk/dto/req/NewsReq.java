package com.softeng.dingtalk.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="NewsReq对象", description="公告请求对象")
public class NewsReq implements Serializable {

    @ApiModelProperty(value = "公告标题")
    private String title;

    @ApiModelProperty(value = "公告链接")
    private String link;

    @ApiModelProperty(value = "公告发布者id")
    private int authorId;
}
