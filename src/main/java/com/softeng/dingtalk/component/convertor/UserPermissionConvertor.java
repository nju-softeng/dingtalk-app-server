package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.UserPermissionReq;
import com.softeng.dingtalk.dto.resp.UserPermissionResp;
import com.softeng.dingtalk.po_entity.UserPermission;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionConvertor extends AbstractConvertorTemplate<UserPermissionReq, UserPermissionResp, UserPermission> {
}

