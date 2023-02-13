package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.convertor.AbstractConvertorTemplate;
import com.softeng.dingtalk.dto.resp.UserPermissionResp;
import com.softeng.dingtalk.entity.UserPermission;
import com.softeng.dingtalk.po.UserPermissionPo;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionConvertor extends AbstractConvertorTemplate<UserPermissionResp, UserPermissionResp, UserPermission, UserPermissionPo> {
}

