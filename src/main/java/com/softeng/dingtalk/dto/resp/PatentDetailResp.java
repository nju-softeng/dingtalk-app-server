package com.softeng.dingtalk.dto.resp;

import com.softeng.dingtalk.po_entity.User;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PatentDetailResp对象", description="专利详情响应对象")
public class PatentDetailResp implements Serializable {
    private int id;
    private int num;
    private UserResp user;
}
