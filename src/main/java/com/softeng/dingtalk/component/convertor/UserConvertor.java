package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.UserReq;
import com.softeng.dingtalk.dto.resp.UserResp;
import com.softeng.dingtalk.po_entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConvertor extends AbstractConvertorTemplate<UserReq, UserResp, User> {

}
