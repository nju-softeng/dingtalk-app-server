package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.PatentDetailReq;
import com.softeng.dingtalk.dto.req.PracticeReq;
import com.softeng.dingtalk.dto.resp.PatentDetailResp;
import com.softeng.dingtalk.dto.resp.PracticeResp;
import com.softeng.dingtalk.po_entity.PatentDetail;
import com.softeng.dingtalk.po_entity.Practice;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PatentDetailConvertor extends AbstractConvertorTemplate<PatentDetailReq, PatentDetailResp, PatentDetail>  {
    @Resource
    private UserConvertor userConvertor;

    @Override
    public PatentDetailResp entity_PO2Resp(PatentDetail patentDetail) {
        return super.entity_PO2Resp(patentDetail).setUser(userConvertor.entity_PO2Resp(patentDetail.getUser()));
    }
}
