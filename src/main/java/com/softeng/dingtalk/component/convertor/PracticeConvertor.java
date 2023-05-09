package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dao.repository.UserRepository;
import com.softeng.dingtalk.dto.req.PracticeReq;
import com.softeng.dingtalk.dto.resp.PracticeResp;
import com.softeng.dingtalk.dto.resp.VMApplyResp;
import com.softeng.dingtalk.po_entity.Practice;
import com.softeng.dingtalk.po_entity.VMApply;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PracticeConvertor extends AbstractConvertorTemplate<PracticeReq, PracticeResp, Practice> {
    @Resource
    private UserConvertor userConvertor;
    @Resource
    private UserRepository userRepository;

    @Override
    public PracticeResp entity_PO2Resp(Practice practice) {
        return super.entity_PO2Resp(practice).setUser(userConvertor.entity_PO2Resp(practice.getUser()));
    }

    @Override
    public Practice req2Entity_PO(PracticeReq practiceReq) {
        return super.req2Entity_PO(practiceReq).setUser(userRepository.findById(practiceReq.getUserId()).get());
    }
}
