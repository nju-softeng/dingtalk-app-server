package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.PatentDetailReq;
import com.softeng.dingtalk.dto.req.PatentReq;
import com.softeng.dingtalk.dto.resp.PatentDetailResp;
import com.softeng.dingtalk.dto.resp.PatentResp;
import com.softeng.dingtalk.po_entity.Patent;
import com.softeng.dingtalk.po_entity.PatentDetail;
import com.softeng.dingtalk.utils.StreamUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PatentConvertor extends AbstractConvertorTemplate<PatentReq, PatentResp, Patent>  {
    @Resource
    private UserConvertor userConvertor;
    @Resource
    private PatentDetailConvertor patentDetailConvertor;

    @Override
    public PatentResp entity_PO2Resp
            (Patent patent) {
        return super.entity_PO2Resp(patent)
                .setApplicant(userConvertor.entity_PO2Resp(patent.getApplicant()))
                .setPatentDetailList(StreamUtils.map(patent.getPatentDetailList(), patentDetail -> patentDetailConvertor.entity_PO2Resp(patentDetail)));
    }
}
