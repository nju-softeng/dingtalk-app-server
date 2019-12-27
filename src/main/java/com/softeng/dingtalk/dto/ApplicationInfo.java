package com.softeng.dingtalk.dto;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.Application;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhanyeye
 * @description  用户前端提交的申请信息
 * @create 12/13/2019 9:17 PM
 */
@Getter
@Setter
@AllArgsConstructor
public class ApplicationInfo {
    private Application application;  //周绩效申请
    private List<AcItem> acItems;     //ac值申请列表
}
