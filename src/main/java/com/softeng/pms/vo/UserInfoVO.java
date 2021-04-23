package com.softeng.pms.vo;

import com.softeng.pms.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhanyeye
 * @description
 * @create 3/27/2020 8:09 PM
 */
@Setter
@Getter
@AllArgsConstructor
public class UserInfoVO {
    private String name;
    private String avatar;
    private Position position;
    private String stuNum;
}
