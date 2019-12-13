package com.softeng.dingtalk.dto;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.Application;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 12/13/2019 9:17 PM
 */
@Getter
@Setter
public class ApplicationInfo {
    private Application application;
    private List<AcItem> acItems;
}
