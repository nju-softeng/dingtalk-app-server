package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.po.ReportApplicantPO;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.vo.CheckedVO;
import com.softeng.dingtalk.vo.ToCheckVO;
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
    @Autowired
    UserService userService;
    @Autowired
    DcAcRelationRepository dcAcRelationRepository;

    /**
     *
     * @param checked
     * @return void
     * @Date 5:22 PM 1/30/2020
     **/
    public void updateAudit(CheckedVO checked) {
        DcRecord dcRecord = dcRecordRepository.findById(checked.getId()).get();
        dcRecord.update(checked.getCvalue(), checked.getDc(), checked.getAc());
        dcRecordRepository.save(dcRecord); //持久化新的 dcRecord
        List<Integer> acIds =  dcAcRelationRepository.listAcId(checked.getId()); // 获取之前的审核对应的ac申请的id
        for (Integer i : acIds) {
            acRecordRepository.deleteById(i); //更新前要删除上一次的 AcRecord 记录
            log.debug("???????");
        }

        for (AcItem acItem : checked.getAcItems()) { //持久化新的 AcRecord
            AcRecord acRecord = new AcRecord(dcRecord, acItem);
            acRecordRepository.save(acRecord);

        }
    }


    
//    /**
//     *  // todo 待修改
//     * @param uid, dateTime
//     * @return java.util.Map
//     * @Date 11:29 AM 1/30/2020
//     **/
//    public Map getReport(int uid, LocalDateTime dateTime) {
//        String userid =  userService.getUserid(uid);
//        return dingTalkUtils.getReport(userid, dateTime);
//    }
    

    /**
     * 审核人获取已审核申请
     * @param uid
     * @return java.util.List<com.softeng.dingtalk.vo.CheckedVO>
     * @Date 8:15 PM 1/28/2020
     **/
    public List<CheckedVO> listCheckVO(int uid) {
        List<DcRecord> dcRecords = dcRecordRepository.listChecked(uid);
        List<CheckedVO> checkedVOS = new ArrayList<>();
        for (DcRecord dc : dcRecords) {
            checkedVOS.add(new CheckedVO(dc.getApplicant().getName(),dc, acItemRepository.findAllByDcRecordID(dc.getId())));
        }
        return checkedVOS;
    }

    /**
     * 审核人查看待审核的申请
     * @param uid 审核人id
     * @return java.util.List<com.softeng.dingtalk.dto.ApplicationInfo>
     * @date 9:37 AM 12/27/2019
     **/
    public List<ToCheckVO> getPendingApplication(int uid) {
        List<ToCheckVO> toCheckVOList = dcRecordRepository.listDcRecordVO(uid);
        for (ToCheckVO toCheckVO : toCheckVOList) {
            toCheckVO.setAcItems(acItemRepository.findAllByDcRecordID(toCheckVO.getId()));
        }
        return toCheckVOList;
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
            futures.add(dingTalkUtils.getReports(u.getUserid(), u.getInsertTime(), u.getUid()));
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
            acRecordRepository.save(a);
            dcAcRelationRepository.save(new DcAcRelation(dc.getId(), a.getId()));
        }


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
