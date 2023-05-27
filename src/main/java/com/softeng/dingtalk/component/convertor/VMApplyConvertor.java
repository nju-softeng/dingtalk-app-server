package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.VMApplyReq;
import com.softeng.dingtalk.dto.resp.VMApplyResp;
import com.softeng.dingtalk.entity.VMApply;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class VMApplyConvertor extends AbstractConvertorTemplate<VMApplyReq, VMApplyResp, VMApply> {
    @Resource
    private UserConvertor userConvertor;

    @Override
    public VMApplyResp entity2Resp(VMApply vmApply) {
        return super.entity2Resp(vmApply).setUser(userConvertor.entity2Resp(vmApply.getUser()));
    }
}
