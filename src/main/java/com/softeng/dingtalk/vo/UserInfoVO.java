package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 3/27/2020 8:09 PM
 */
@Setter
@Getter
@AllArgsConstructor
public class UserInfoVO {
    private int id;
    private String name;
    private List<String> dept;
}
