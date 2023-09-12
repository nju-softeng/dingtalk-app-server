package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.ReimbursementReq;
import com.softeng.dingtalk.dto.resp.ReimbursementResp;
import com.softeng.dingtalk.entity.Reimbursement;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ReimbursementConvertor extends AbstractConvertorTemplate<ReimbursementReq, ReimbursementResp, Reimbursement> {
    @Resource
    private UserConvertor userConvertor;
    @Override
    public ReimbursementResp entity2Resp(Reimbursement reimbursement) {
        return super.entity2Resp(reimbursement).setUser(userConvertor.entity2Resp(reimbursement.getUser()));
    }
}
