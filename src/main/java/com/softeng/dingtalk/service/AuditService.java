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
import java.time.LocalDate;
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


    /**
     * 审核人更新绩效申请
     * @param checked
     * @return void
     * @Date 4:41 PM 2/1/2020
     **/
    public void updateAudit(CheckedVO checked) {
        DcRecord dc = dcRecordRepository.findById(checked.getId()).get();
        dc.update(checked.getCvalue(), checked.getDc(), checked.getAc()); // 更新 cvalue, dc, ac
        dcRecordRepository.save(dc); //持久化新的 dcRecord
        acItemRepository.deleteByDcRecord(dc); //删除旧数据
        for (AcItem acItem : checked.getAcItems()) {
            acItem.setDcRecord(dc);
            if (acItem.isStatus()) {
                AcRecord acRecord = acRecordRepository.save(new AcRecord(dc, acItem));
                acItem.setAcRecord(acRecord);
            }
        }
        acItemRepository.saveAll(checked.getAcItems());
        //更新DcSummary数据
        Double dcSum = dcRecordRepository.getUserWeekTotalDc(dc.getApplicant().getId(), dc.getYearmonth(), dc.getWeek());
        DcSummary dcSummary = dcSummaryRepository.getDcSummary(dc.getApplicant().getId(), dc.getYearmonth());
        if (dcSummary == null) {
            dcSummary = new DcSummary(dc.getApplicant(), dc.getYearmonth());
        }
        dcSummary.updateWeek(dc.getWeek(), dcSum);
        dcSummaryRepository.save(dcSummary);
    }

    /**
     * 审核人提交的审核结果
     * @param reviewVO
     * @return void
     * @Date 12:04 PM 1/28/2020
     **/
    public void addAuditResult(ReviewVO reviewVO) {
        DcRecord dc = dcRecordRepository.findById(reviewVO.getId()).get();
        dc.update(reviewVO.getCvalue(), reviewVO.getDc(),reviewVO.getAc());
        dcRecordRepository.save(dc);
        for (AcItem a : reviewVO.getAcItems()) {
            a.setDcRecord(dc); //前端传来的没有dcRecord属性
            if (a.isStatus()) {
                AcRecord acRecord = acRecordRepository.save(new AcRecord(dc, a));
                a.setAcRecord(acRecord);
            }
        }
        acItemRepository.saveAll(reviewVO.getAcItems());
        //更新DcSummary数据
        Double dcSum = dcRecordRepository.getUserWeekTotalDc(dc.getApplicant().getId(), dc.getYearmonth(), dc.getWeek());
        DcSummary dcSummary = dcSummaryRepository.getDcSummary(dc.getApplicant().getId(), dc.getYearmonth());
        if (dcSummary == null) {
            dcSummary = new DcSummary(dc.getApplicant(), dc.getYearmonth());
        }
        dcSummary.updateWeek(dc.getWeek(), dcSum);
        dcSummaryRepository.save(dcSummary);
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
            checkedVOS.add(new CheckedVO(dc, acItemRepository.findAllByDcRecordID(dc.getId())));
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
     * 异步获取多个周报内容
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


//    public Map getReport(int dcRecordid) {
//        DcRecord dc = dcRecordRepository.findById(dcRecordid).get();
//        String usercode = dc.getApplicant().getUserid();
//        LocalDate date = dc.getWeekdate();
//
//
//
//    }




}
