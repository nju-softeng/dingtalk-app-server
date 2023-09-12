package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dao.repository.UserRepository;
import com.softeng.dingtalk.dto.req.PracticeReq;
import com.softeng.dingtalk.dto.resp.PracticeResp;
import com.softeng.dingtalk.entity.Practice;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PracticeConvertor extends AbstractConvertorTemplate<PracticeReq, PracticeResp, Practice> {
    @Resource
    private UserConvertor userConvertor;
    @Resource
    private UserRepository userRepository;

    @Override
    public PracticeResp entity2Resp(Practice practice) {
        return super.entity2Resp(practice).setUser(userConvertor.entity2Resp(practice.getUser()));
    }

    @Override
    public Practice req2Entity(PracticeReq practiceReq) {
        return super.req2Entity(practiceReq).setUser(userRepository.findById(practiceReq.getUserId()).get());
    }
}
