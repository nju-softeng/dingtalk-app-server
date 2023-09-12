package com.softeng.dingtalk.dto.req;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="InternshipPeriodRecommendedReq对象", description="推荐实习周期请求对象")
public class InternshipPeriodRecommendedReq implements Serializable {
    private int id;

    private LocalDate start;

    private LocalDate end;

    private LocalDateTime releaseTime;

    private int authorId;
}
