package com.softeng.pms.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhanyeye
 * @description
 * @create 3/22/2020 2:22 PM
 */
@AllArgsConstructor
@Getter
@Setter
public class BugCheckVO {
    private int id;
    private boolean status;
    private int iterationId;
    private int uid;
}
