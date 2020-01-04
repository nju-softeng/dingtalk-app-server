package com.softeng.dingtalk.service;

import com.softeng.dingtalk.dto.ApplicationInfo;
import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.entity.DcSummary;
import com.softeng.dingtalk.repository.AcItemRepository;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.repository.DcSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description 周绩效审核业务逻辑
 * @create 12/26/2019 3:34 PM
 */
@Service
@Transactional
@Slf4j
public class AuditService {
    @Autowired
    AcItemRepository acItemRepository;
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    DcSummaryRepository dcSummaryRepository;


    /**
     * 审核人查看待审核的申请
     * @param uid 审核人id
     * @return java.util.List<com.softeng.dingtalk.dto.ApplicationInfo>
     * @date 9:37 AM 12/27/2019
     **/
    public List<ApplicationInfo> getPendingApplication(int uid) {
        List<DcRecord> dcRecords = dcRecordRepository.listPendingReview(uid);
        List<ApplicationInfo> applicationInfos = new ArrayList<>();
        for (int i = 0; i < dcRecords.size(); i++) {
            applicationInfos.add(new ApplicationInfo(dcRecords.get(i), acItemRepository.findAllByDcRecordID(dcRecords.get(i).getId())));
        }
        return applicationInfos;
    }

    /**
     * 审核人提交的审核结果 (DcRecord, AcRecords)
     * @param dcRecord
     * @param acRecords
     * @return void
     * @date 10:03 AM 12/27/2019
     **/
    public void addAuditResult(DcRecord dcRecord, List<AcRecord> acRecords) {
        //todo 担心空指针！！！
        dcRecordRepository.updateById(dcRecord.getId(), dcRecord.getCvalue(), dcRecord.getDc());
        DcRecord dc = dcRecordRepository.getbyId(dcRecord.getId());  //这里的dc只返回了applicant, yearmonth, week 字段，其他字段为空
        acRecordRepository.saveAll(acRecords);  //持久化多个AC记录

        //更新DcSummary数据
        Double dcSum = dcRecordRepository.getUserWeekTotalDc(dc.getApplicant().getId(), dc.getYearmonth(), dc.getWeek());
        DcSummary dcSummary = dcSummaryRepository.getDcSummary(dc.getApplicant().getId(), dc.getYearmonth());
        if (dcSummary == null) {
            dcSummary = new DcSummary(dc.getApplicant(), dc.getYearmonth());
        }
        dcSummary.updateWeek(dc.getWeek(), dcSum);
        dcSummaryRepository.save(dcSummary);
    }

}
