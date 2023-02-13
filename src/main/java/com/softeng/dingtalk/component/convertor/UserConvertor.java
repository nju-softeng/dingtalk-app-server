package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.convertor.AbstractConvertorTemplate;
import com.softeng.dingtalk.dto.req.UserReq;
import com.softeng.dingtalk.dto.resp.UserResp;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.po.UserPo;
import org.springframework.stereotype.Component;

@Component
public class UserConvertor extends AbstractConvertorTemplate<UserReq, UserResp, User, UserPo> {

}
