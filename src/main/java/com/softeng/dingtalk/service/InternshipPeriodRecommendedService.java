package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.convertor.InternshipPeriodRecommendedConvertor;
import com.softeng.dingtalk.dao.repository.InternshipPeriodRecommendedRepository;
import com.softeng.dingtalk.dto.req.InternshipPeriodRecommendedReq;
import com.softeng.dingtalk.dto.resp.InternshipPeriodRecommendedResp;
import com.softeng.dingtalk.exception.CustomExceptionEnum;
import com.softeng.dingtalk.po_entity.InternshipPeriodRecommended;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class InternshipPeriodRecommendedService {
    @Resource
    private InternshipPeriodRecommendedRepository internshipPeriodRecommendedRepository;
    @Resource
    private InternshipPeriodRecommendedConvertor internshipPeriodRecommendedConvertor;


    public InternshipPeriodRecommendedResp getNewestPeriod() {
        InternshipPeriodRecommended res = internshipPeriodRecommendedRepository.findTop();
        CustomExceptionEnum.INTERNSHIP_PERIOD_UNSET.throwIf(res == null);
        return internshipPeriodRecommendedConvertor.entity_PO2Resp(res);
    }

    public void addPeriod(InternshipPeriodRecommendedReq internshipPeriodRecommendedReq) {
        internshipPeriodRecommendedRepository.save(internshipPeriodRecommendedConvertor.req2Entity_PO(internshipPeriodRecommendedReq));
    }

}
