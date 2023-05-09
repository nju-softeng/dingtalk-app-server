package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.ReimbursementReq;
import com.softeng.dingtalk.dto.resp.PracticeResp;
import com.softeng.dingtalk.dto.resp.ReimbursementResp;
import com.softeng.dingtalk.po_entity.Practice;
import com.softeng.dingtalk.po_entity.Reimbursement;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ReimbursementConvertor extends AbstractConvertorTemplate<ReimbursementReq, ReimbursementResp, Reimbursement> {
    @Resource
    private UserConvertor userConvertor;
    @Override
    public ReimbursementResp entity_PO2Resp(Reimbursement reimbursement) {
        return super.entity_PO2Resp(reimbursement).setUser(userConvertor.entity_PO2Resp(reimbursement.getUser()));
    }
}
