package com.softeng.dingtalk.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhanyeye
 * @description
 * @create 26/06/2020 7:54 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorVO {
    private int uid;
    private String name;
    private int num;
}
