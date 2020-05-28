package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhanyeye
 * @description 根据条件查询用户的VO
 * @create 3/28/2020 10:19 AM
 */
@AllArgsConstructor
@Getter
@Setter
public class QueryUserVO {
    private String name;
    private Position position;
}
