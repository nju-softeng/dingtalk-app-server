package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.PermissionReq;
import com.softeng.dingtalk.dto.resp.PermissionResp;
import com.softeng.dingtalk.po_entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionConvertor extends AbstractConvertorTemplate<PermissionReq, PermissionResp, Permission> {
}
