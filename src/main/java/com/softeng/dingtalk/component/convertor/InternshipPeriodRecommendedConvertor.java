package com.softeng.dingtalk.component.convertor;

import com.softeng.dingtalk.dto.req.InternshipPeriodRecommendedReq;
import com.softeng.dingtalk.dto.resp.InternshipPeriodRecommendedResp;
import com.softeng.dingtalk.entity.InternshipPeriodRecommended;
import com.softeng.dingtalk.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InternshipPeriodRecommendedConvertor extends AbstractConvertorTemplate<InternshipPeriodRecommendedReq, InternshipPeriodRecommendedResp, InternshipPeriodRecommended>{
    @Override
    public InternshipPeriodRecommended req2Entity(InternshipPeriodRecommendedReq internshipPeriodRecommendedReq) {
        InternshipPeriodRecommended res = super.req2Entity(internshipPeriodRecommendedReq);
        res.setAuthor(new User().setId(internshipPeriodRecommendedReq.getAuthorId()));
//        这里加上时间，因为News有release_time字段，且为null，
//        会让数据库表的default CURRENT_TIMESTAMP失效
        res.setReleaseTime(LocalDateTime.now());
        return res;
    }
}
