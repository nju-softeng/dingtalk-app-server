package com.softeng.dingtalk.dto.resp;

import com.softeng.dingtalk.entity.User;
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
@ApiModel(value="InternshipPeriodRecommendedResp对象", description="推荐实习周期响应对象")
public class InternshipPeriodRecommendedResp implements Serializable {
    private int id;

    private LocalDate start;

    private LocalDate end;

    private LocalDateTime releaseTime;

    private User author;
}
