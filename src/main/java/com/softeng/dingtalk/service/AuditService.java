package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.po.ReportApplicantPO;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.vo.CheckedVO;
import com.softeng.dingtalk.vo.ToCheckVO;
import com.softeng.dingtalk.vo.CheckVO;
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



    /**
     * 审核人更新绩效申请
     * @param checked
     * @return void
     * @Date 4:41 PM 2/1/2020
     **/
    public DcRecord updateAudit(CheckedVO checked) {
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
        return dc;
    }

    /**
     * 审核人提交的审核结果
     * @param checkVO
     * @return void
     * @Date 12:04 PM 1/28/2020
     **/
    public DcRecord addAuditResult(CheckVO checkVO) {
        DcRecord dc = dcRecordRepository.findById(checkVO.getId()).get();
        dc.update(checkVO.getCvalue(), checkVO.getDc(), checkVO.getAc());
        dcRecordRepository.save(dc);
        for (AcItem a : checkVO.getAcItems()) {
            a.setDcRecord(dc); //前端传来的没有dcRecord属性
            if (a.isStatus()) {
                AcRecord acRecord = acRecordRepository.save(new AcRecord(dc, a));
                a.setAcRecord(acRecord);
            }
        }
        acItemRepository.saveAll(checkVO.getAcItems());
        return dc;
    }



    //更新DcSummary数据
    public void updateDcSummary(DcRecord dc) {
        log.debug("update DcSummary" + dc.getId());
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
        List<CheckedVO> checkedVOS = dcRecordRepository.listChecked(uid);
        for (CheckedVO checked : checkedVOS) {
            checked.setAcItems(acItemRepository.findAllByDcRecordID(checked.getId()));
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
        List<ToCheckVO> toCheckVOList = dcRecordRepository.listToCheckVO(uid);
        for (ToCheckVO toCheckVO : toCheckVOList) {
            toCheckVO.setAcItems(acItemRepository.findAllByDcRecordID(toCheckVO.getId()));
        }
        return toCheckVOList;
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
