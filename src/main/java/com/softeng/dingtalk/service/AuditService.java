package com.softeng.dingtalk.service;

import com.softeng.dingtalk.dto.ApplicationInfo;
import com.softeng.dingtalk.dto.AuditInfo;
import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.Application;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.repository.AcItemRepository;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.ApplicationRepository;
import com.softeng.dingtalk.repository.DcRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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
    ApplicationRepository applicationRepository;
    @Autowired
    AcItemRepository acItemRepository;
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    PerformanceService performanceService;

    /**
     * 审核人查看待审核的申请
     * @param uid 审核人id
     * @return java.util.List<com.softeng.dingtalk.dto.ApplicationInfo>
     * @date 9:37 AM 12/27/2019
     **/
    public List<ApplicationInfo> getPendingApplication(int uid) {
        List<Application> applications = applicationRepository.listAuditorPendingApplication(uid);
        List<ApplicationInfo> applicationInfos = new ArrayList<>();
        for (int i = 0; i < applications .size(); i++) {
            applicationInfos.add(new ApplicationInfo(applications.get(i), acItemRepository.findAllByApplicationID(applications.get(i).getId())));
        }
        return applicationInfos;
    }

    /**
     * 审核人提交的审核结果 (DcRecord, AcRecords)
     * @param auditInfo 审核结果信息(dto) //todo 需更新
     * @return void
     * @date 10:03 AM 12/27/2019
     **/
    public void addAuditResult(DcRecord dcRecord, List<AcRecord> acRecords) {
        dcRecordRepository.save(dcRecord);     //持久化DC记录
        acRecordRepository.saveAll(acRecords);  //持久化多个AC记录
        applicationRepository.updateApplicationCheckStatus(dcRecord.getApplication().getId());  // 将申请状态从false变成true
        performanceService.updateWeekTotalDc(dcRecord.getUser().getId(), dcRecord.getCreateTime(), dcRecord.getWeek());
    }

//    public List<AcRecord> getAcRecord(int uid) {
//        return acRecordRepository.getAcRecordsByAuditor(new User());
//    }


    public void updateDcRecord(double dc) {

    }
}
