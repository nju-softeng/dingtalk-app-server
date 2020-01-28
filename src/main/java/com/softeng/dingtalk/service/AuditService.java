package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.entity.DcSummary;
import com.softeng.dingtalk.po.ReportApplicantPO;
import com.softeng.dingtalk.repository.AcItemRepository;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.repository.DcSummaryRepository;
import com.softeng.dingtalk.vo.ApplicationVO;
import com.softeng.dingtalk.vo.DcRecordVO;
import com.softeng.dingtalk.vo.ReviewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    @Autowired
    DingTalkUtils dingTalkUtils;


    /**
     * 审核人查看待审核的申请
     * @param uid 审核人id
     * @return java.util.List<com.softeng.dingtalk.dto.ApplicationInfo>
     * @date 9:37 AM 12/27/2019
     **/
    public List<ApplicationVO> getPendingApplication(int uid) {
        List<DcRecordVO> dcRecordVOList = dcRecordRepository.listDcRecordVO(uid);
        List<ApplicationVO> applicationVOS = new ArrayList<>();
        for ( DcRecordVO dcRecordVO : dcRecordVOList) {
            applicationVOS.add(new ApplicationVO(dcRecordVO, acItemRepository.findAllByDcRecordID(dcRecordVO.getId())));
        }
        return applicationVOS;
    }


    /**
     * 获取周报内容
     * @param uid
     * @return java.util.List<java.lang.Object>
     * @Date 2:50 PM 1/26/2020
     **/
    public List<Object> AsyncGetReport(int uid) throws ExecutionException, InterruptedException {
        List<ReportApplicantPO> reportApplicantPOS = dcRecordRepository.listUserCode(uid);
        List<Future<Map>> futures = new ArrayList<>();
        for (ReportApplicantPO u : reportApplicantPOS) {
            futures.add(dingTalkUtils.getReport(u.getUserid(), u.getInsertTime(), u.getUid()));
        }
        List<Object> result = new ArrayList<>();
        for (Future future : futures) {
            result.add(future.get());
        }
        return result;
    }


    /**
     * 审核人提交的审核结果
     * @param reviewVO
     * @return void
     * @Date 12:04 PM 1/28/2020
     **/
    public void addAuditResult(ReviewVO reviewVO) {
        //todo 担心空指针！！！
        DcRecord dc = dcRecordRepository.findById(reviewVO.getId()).get();
        dc.update(reviewVO.getCvalue(), reviewVO.getDc(),reviewVO.getAc());
        for (AcRecord a : reviewVO.getAcRecords()) {
            a.setUser(dc.getApplicant());
            a.setAuditor(dc.getAuditor());
        }
        acRecordRepository.saveAll(reviewVO.getAcRecords());  //持久化多个AC记录

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
