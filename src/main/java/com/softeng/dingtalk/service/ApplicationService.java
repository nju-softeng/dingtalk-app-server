package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.DateUtils;
import com.softeng.dingtalk.po_entity.AcItem;
import com.softeng.dingtalk.po_entity.AcRecord;
import com.softeng.dingtalk.po_entity.DcRecord;
import com.softeng.dingtalk.dao.mapper.DcRecordMapper;
import com.softeng.dingtalk.dao.repository.AcItemRepository;
import com.softeng.dingtalk.dao.repository.AcRecordRepository;
import com.softeng.dingtalk.dao.repository.DcRecordRepository;
import com.softeng.dingtalk.vo.ApplyVO;
import com.softeng.dingtalk.vo.DcRecordVO;
import com.softeng.dingtalk.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description 周绩效申请业务逻辑
 * @create 12/11/2019 2:35 PM
 */
@Service
@Transactional
@Slf4j
public class ApplicationService {
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    AcItemRepository acItemRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    DcRecordMapper dcRecordMapper;
    @Autowired
    AuditService auditService;
    @Autowired
    NotifyService notifyService;
    @Autowired
    DateUtils dateUtils;

    // 自己注入自己防止事务失效
    @Autowired
    ApplicationService applicationService;

    /**
     * 持久化dc（周绩效申请）, ac（学术学分）申请，并将绩dc作为ac的外键
     * @param acItems 学术学分申请项
     * @param dc 周绩效申请
     */
    public void saveAcItemsAndDcRecord(List<AcItem> acItems, DcRecord dc) {
        dcRecordRepository.save(dc);
        acItems.forEach(acItem -> acItem.setDcRecord(dc));
        acItemRepository.saveAll(acItems);
    }


    /**
     * 添加新的绩效申请
     * @param vo 申请信息的值对象
     * @param uid 申请人的id
     */
    public void addApplication(ApplyVO vo, int uid) {
        int dateCode = dateUtils.getDateCode(vo.getDate());
        assertUniqueConstraintException(uid, vo.getAuditorid(), vo.getId(), dateCode);
        assertTimeException(vo.getDate());

        applicationService.saveAcItemsAndDcRecord(vo.getAcItems(), new DcRecord(uid, vo, dateCode));
    }


    /**
     * 更新已提交的绩效申请
     * @param vo 申请信息包装对象
     * @param uid 申请人的id
     */
    public void updateApplication(ApplyVO vo, int uid) {
        int dateCode = dateUtils.getDateCode(vo.getDate());
        assertUniqueConstraintException(uid, vo.getAuditorid(), vo.getId(), dateCode);

        DcRecord dc = dcRecordRepository.findById(vo.getId()).get();
        dc.reApply(vo.getAuditorid(), vo.getDvalue(), vo.getDate(), dateCode);

        // 删除之前的acItems记录
        acItemRepository.deleteByDcRecord(dc);
        applicationService.saveAcItemsAndDcRecord(vo.getAcItems(), dc);
    }


    /**
     * 持久化ac申请，并将绩效申请作为外键
     * @param acItems
     * @param dc
     */
    public void saveAcRecordsWithDcIdAsForeignKey(List<AcItem> acItems, DcRecord dc) {
        acItems.forEach(acItem -> {
            acItem.setDcRecord(dc);
            AcRecord acRecord = acRecordRepository.save(new AcRecord(dc, acItem, dc.getInsertTime()));
            acItem.setAcRecord(acRecord);
        });
        acItemRepository.saveAll(acItems);
    }


    /**
     * 审核人更新DcRecord,AcRecords,DcSummary,并发送更新DcMessage结果消息
     * @param dc
     * @param acItems
     */
    public void auditorUpdateDcRecordAndAcRecords(DcRecord dc, List<AcItem> acItems) {
        dcRecordRepository.save(dc);
        dcRecordRepository.refresh(dc);
        applicationService.saveAcRecordsWithDcIdAsForeignKey(acItems, dc);
        auditService.updateDcSummary(dc.getApplicant().getId(), dc.getYearmonth(), dc.getWeek());
        notifyService.updateDcMessage(dc);
    }


    /**
     * 审核人添加新的绩效申请 (免审核)
     * @param vo
     * @param uid
     */
    public void addApplicationByAuditor(ApplyVO vo, int uid) {
        int dateCode = dateUtils.getDateCode(vo.getDate());
        assertUniqueConstraintException(uid, vo.getAuditorid(), vo.getId(), dateCode);
        assertTimeException(vo.getDate());

        DcRecord dc = new DcRecord().setByAuditor(uid, vo, dateCode);
        applicationService.auditorUpdateDcRecordAndAcRecords(new DcRecord().setByAuditor(uid, vo, dateCode), vo.getAcItems());
    }


    /**
     * 审核人更新已提交的绩效申请
     * @param vo
     * @param uid
     */
    public void updateApplicationByAuditor(ApplyVO vo, int uid) {
        int dateCode = dateUtils.getDateCode(vo.getDate());
        assertUniqueConstraintException(uid, vo.getAuditorid(), vo.getId(), dateCode);

        DcRecord dc = dcRecordRepository.findById(vo.getId()).get();
        dc.setByAuditor(uid, vo, dateCode);

        // 删除旧的AcItems，同时级联删除相关AcRecord:见AcItem实体类
        acItemRepository.deleteByDcRecord(dc);
        applicationService.auditorUpdateDcRecordAndAcRecords(dc, vo.getAcItems());
    }


    /**
     * 确保一周只能向同一审核人提交一次申请
     * @param uid 申请者id
     * @param aid 审核人id
     * @param vid 提交的申请记录id
     */
    private void assertUniqueConstraintException(int uid, int aid, int vid, int dateCode) {
        int yearmonth = dateCode / 10;
        int week = dateCode % 10;
        // 是否本周已经提交记录，如果有则获得它的id，否则为null
        Integer hasExist = isExist(uid, aid, yearmonth, week);
        // 如果本周已经提交记录 && 不是对已提交的记录进行修改
        if (hasExist != null && hasExist != vid) {
            // 一周只能向审核人提交一次
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "每周只能向同一个审核人提交一次申请");
        }
    }


    /**
     * 判断是否在指定时间申请
     * @param date
     */
    private void assertTimeException(LocalDate date) {
//        long gap = date.until(LocalDate.now(), ChronoUnit.DAYS);
//        if (gap > 10) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "过期一周不支持补登记！");
//        } else if (gap < 0) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "选中的时间还没到，不可以穿越的 ~");
//        }
    }


    /**
     * 分页获取提交过的申请
     * @param uid
     * @param page
     * @return
     */
    public Map listDcRecord(int uid, int page, int size) {
        int offset = (page - 1) * size;
        List<DcRecordVO> dcRecordList = dcRecordMapper.listDcRecordVO(uid, offset, size);
        int total = dcRecordMapper.countDcRecordByuid(uid);
        return Map.of("list", dcRecordList, "total", total);
    }


    /**
     * 用户每周只能向同一审核人提交一个申请，判断数据库中是否已存在
     * @param uid 申请人id
     * @param aid 审核人id
     * @param yearmonth 年月
     * @param week 第几周
     * @return
     */
    public Integer isExist(int uid, int aid, int yearmonth, int week) {
        return dcRecordRepository.isExist(uid, aid, yearmonth, week);
    }


    /**
     * 查询申请人最近一次绩效申请提交给谁
     * @param uid 申请人id
     * @return 审核人信息的包装对象
     */
    public UserVO getRecentAuditor(int uid) {
        return dcRecordMapper.getRecentAuditorByApplicantId(uid);
    }
}
