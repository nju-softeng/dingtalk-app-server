package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.vo.CheckedVO;
import com.softeng.dingtalk.vo.ToCheckVO;
import com.softeng.dingtalk.vo.CheckVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    DingTalkUtils dingTalkUtils;
    @Autowired
    UserService userService;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    NotifyService notifyService;


    // 审核人提交或更新绩效申请
    public DcRecord submitAudit(CheckVO checkVO) {
        DcRecord dc = dcRecordRepository.findById(checkVO.getId()).get();
        if (dc.isStatus()) {
            // status为真，表示此次提交为更新 -> 删除旧的AcItems， 同时级联删除相关AcRecord
            acItemRepository.deleteByDcRecord(dc);
        }
        // 更新 cvalue, dc, ac
        dc.update(checkVO.getCvalue(), checkVO.getDc(), checkVO.getAc());
        dcRecordRepository.save(dc); // 持久化新的 dcRecord
        for (AcItem acItem : checkVO.getAcItems()) {
            // 前端传来的没有dcRecord属性, 手动添加
            acItem.setDcRecord(dc);
            if (acItem.isStatus()) {
                // ac申请被同意
                AcRecord acRecord = acRecordRepository.save(new AcRecord(dc, acItem));
                acItem.setAcRecord(acRecord);
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
        notifyService.updateDcMessage(dc);  // 发送消息
        performanceService.computeSalary(dc.getApplicant().getId(), dc.getYearmonth());  //重新计算助研金
    }


    // 审核人分页获取已审核申请
    public Map listCheckedVO(int uid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<CheckedVO> pages = dcRecordRepository.listChecked(uid, pageable);

        List<CheckedVO> content = pages.getContent();
        for (CheckedVO checked : content) {
            checked.setAcItems(acItemRepository.findAllByDcRecordID(checked.getId()));
        }
        return Map.of("content", content, "total", pages.getTotalElements());
    }

    // 审核人根据时间筛选已审核申请
    public List<CheckedVO> listCheckedByDate(int uid, int yearmonth, int week) {
        List<CheckedVO> checkedVOS = dcRecordRepository.listCheckedByDate(uid, yearmonth, week);
        for (CheckedVO checked : checkedVOS) {
            checked.setAcItems(acItemRepository.findAllByDcRecordID(checked.getId()));
        }
        return checkedVOS;
    }


    // 审核人查看待审核的申请
    public List<ToCheckVO> getPendingApplication(int uid) {
        List<ToCheckVO> toCheckVOList = dcRecordRepository.listToCheckVO(uid);
        for (ToCheckVO toCheckVO : toCheckVOList) {
            toCheckVO.setAcItems(acItemRepository.findAllByDcRecordID(toCheckVO.getId()));
        }
        return toCheckVOList;
    }


    // 查询审核人未审核数
    public int getUnCheckCnt(int aid) {
        return dcRecordRepository.getUnCheckCntByAid(aid);
    }


}
