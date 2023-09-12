package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.PatentDetailReq;
import com.softeng.dingtalk.dto.resp.PatentDetailResp;
import com.softeng.dingtalk.entity.PatentDetail;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PatentDetailConvertor extends AbstractConvertorTemplate<PatentDetailReq, PatentDetailResp, PatentDetail>  {
    @Resource
    private UserConvertor userConvertor;

    @Override
    public PatentDetailResp entity2Resp(PatentDetail patentDetail) {
        return super.entity2Resp(patentDetail).setUser(userConvertor.entity2Resp(patentDetail.getUser()));
    }
}
