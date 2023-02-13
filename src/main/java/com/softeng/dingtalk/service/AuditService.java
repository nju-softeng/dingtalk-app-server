package com.softeng.dingtalk.service;

import com.softeng.dingtalk.dao.repository.AcItemRepository;
import com.softeng.dingtalk.dao.repository.AcRecordRepository;
import com.softeng.dingtalk.dao.repository.DcRecordRepository;
import com.softeng.dingtalk.dao.repository.DcSummaryRepository;
import com.softeng.dingtalk.po.*;
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
import java.util.Optional;


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
    UserService userService;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    NotifyService notifyService;

    // 自己注入自己防止事务失效
    @Autowired
    AuditService auditService;


    /**
     * 持久化被审核通过的 acItems
     * @param acItemPos
     * @param dc
     */
    public void saveCheckedAcRecord(List<AcItemPo> acItemPos, DcRecordPo dc) {
        acItemPos.forEach(acItem -> {
            // 前端传来的没有dcRecord属性, 手动添加
            acItem.setDcRecord(dc);
            if (acItem.isStatus()) {
                // ac申请被同意
                AcRecordPo acRecordPO = acRecordRepository.save(new AcRecordPo(dc, acItem, dc.getInsertTime()));
                acItem.setAcRecord(acRecordPO);
            }
        });
        acItemRepository.saveAll(acItemPos);
    }


    /**
     * 更新审核结果, 更新dcsummary, 插入应用通知
     * @param checkVO 审核人提交的审核结果
     * @return
     */
    public DcRecordPo updateAuditResult(CheckVO checkVO) {
        DcRecordPo dc = dcRecordRepository.findById(checkVO.getId()).get();
        if (dc.isStatus()) {
            // status为真，表示之前审核过，此次提交为更新, 删除旧的AcItems， 同时级联删除相关AcRecord
            acItemRepository.deleteByDcRecord(dc);
        }
        // 更新 cvalue, dc, ac
        dc.update(checkVO.getCvalue(), checkVO.getDc(), checkVO.getAc());
        auditService.saveCheckedAcRecord(checkVO.getAcItemPos(), dc);
        // 更新dcsummary
        auditService.updateDcSummary(dc.getApplicant().getId(), dc.getYearmonth(), dc.getWeek());
        // 发送消息
        notifyService.updateDcMessage(dc);
        return dc;
    }


    /**
     * 更新用户指定周的dc值
     * @param uid 用户id
     * @param yearmonth 所在年月
     * @param week 所在周
     */
    public void updateDcSummary(int uid, int yearmonth, int week) {
        DcSummaryPo dcSummaryPo = Optional.ofNullable(dcSummaryRepository.getDcSummary(uid, yearmonth))
                .orElse(new DcSummaryPo(uid, yearmonth));
        dcSummaryPo.updateWeek(week, dcRecordRepository.getUserWeekTotalDc(uid, yearmonth, week));
        dcSummaryRepository.save(dcSummaryPo);
        performanceService.computeSalary(uid, yearmonth);
    }


    /**
     * 审核人分页获取已审核申请
     * @param uid
     * @param page
     * @param size
     * @return
     */
    public Map listCheckedVO(int uid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<CheckedVO> pages = dcRecordRepository.listChecked(uid, pageable);
        List<CheckedVO> contents = pages.getContent();
        contents.forEach(checked -> {
            checked.setAcItemPos(acItemRepository.findAllByDcRecordID(checked.getId()));
        });
        return Map.of(
                "content", contents,
                "total", pages.getTotalElements()
        );
    }


    /**
     * 审核人根据时间筛选已审核申请
     * @param uid
     * @param yearmonth
     * @param week
     * @return
     */
    public List<CheckedVO> listCheckedByDate(int uid, int yearmonth, int week) {
        List<CheckedVO> checkedVOS = dcRecordRepository.listCheckedByDate(uid, yearmonth, week);
        checkedVOS.forEach(vo -> {
            vo.setAcItemPos(acItemRepository.findAllByDcRecordID(vo.getId()));
        });
        return checkedVOS;
    }


    /**
     * 审核人查看待审核的申请
     * @param uid 审核人的id
     * @return
     */
    public List<ToCheckVO> listPendingApplication(int uid) {
        List<ToCheckVO> toCheckVOList = dcRecordRepository.listToCheckVO(uid);
        toCheckVOList.forEach(toCheckVO -> {
            toCheckVO.setAcItemPos(acItemRepository.findAllByDcRecordID(toCheckVO.getId()));
        });
        return toCheckVOList;
    }


    /**
     * 查询审核人未审核数
     * @param aid 审核人id
     * @return
     */
    public int getUnCheckCnt(int aid) {
        return dcRecordRepository.getUnCheckCntByAid(aid);
    }


}
