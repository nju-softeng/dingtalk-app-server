package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.convertor.AbstractConvertorTemplate;
import com.softeng.dingtalk.dto.req.PermissionReq;
import com.softeng.dingtalk.dto.resp.PermissionResp;
import com.softeng.dingtalk.entity.Permission;
import com.softeng.dingtalk.po.PermissionPo;
import org.springframework.stereotype.Component;

@Component
public class PermissionConvertor extends AbstractConvertorTemplate<PermissionReq, PermissionResp, Permission, PermissionPo> {
}
