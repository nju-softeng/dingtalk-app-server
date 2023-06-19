package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.PatentReq;
import com.softeng.dingtalk.dto.resp.PatentResp;
import com.softeng.dingtalk.entity.Patent;
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
    public PatentResp entity2Resp
            (Patent patent) {
        return super.entity2Resp(patent)
                .setApplicant(userConvertor.entity2Resp(patent.getApplicant()))
                .setPatentDetailList(StreamUtils.map(patent.getPatentDetailList(), patentDetail -> patentDetailConvertor.entity2Resp(patentDetail)));
    }
}
