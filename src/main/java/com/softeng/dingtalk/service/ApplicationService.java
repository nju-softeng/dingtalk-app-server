package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.mapper.DcRecordMapper;
import com.softeng.dingtalk.repository.AcItemRepository;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.vo.ApplyVO;
import com.softeng.dingtalk.vo.DcRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    Utils utils;
    @Autowired
    DcRecordMapper dcRecordMapper;
    @Autowired
    AcRecordRepository acRecordRepository;

    /**
     * 添加 / 更新 申请
     * @param vo
     * @param uid
     * @return
     */
    public void submitApplication(ApplyVO vo, int uid) {
        // 获取申请时间的时间标志, 数组大小为2, result[0]: yearmonth, result[1] week
        int[] result = utils.getTimeFlag(vo.getDate());
        int dateCode = utils.getTimeCode(vo.getDate());

        // 断言 确保一周只能向审核人提交一次
        assertException(uid, vo.getAuditorid(), vo.getId(), result[0], result[1]);

        if (vo.getId() == 0) {
            // 提交的是新的申请

            assertTimeException(vo.getDate());

            DcRecord dc = DcRecord.builder().applicant(new User(uid)).auditor(new User(vo.getAuditorid()))
                    .dvalue(vo.getDvalue()).ac(vo.getAc()).weekdate(vo.getDate()).yearmonth(result[0])
                    .week(result[1]).dateCode(dateCode).build();
            dcRecordRepository.save(dc);
            for (AcItem acItem : vo.getAcItems()) {
                // 持久化ac申请，并将绩效申请作为外键
                acItem.setDcRecord(dc);
            }
            acItemRepository.saveAll(vo.getAcItems());
        } else {
            DcRecord dc = dcRecordRepository.findById(vo.getId()).get();
            dc.reApply(vo.getAuditorid(), vo.getDvalue(), vo.getDate(), result[0], result[1], dateCode);
            dcRecordRepository.save(dc);
            // 强制存入数据库
            dcRecordRepository.flush();
            //删除之前的记录
            acItemRepository.deleteByDcRecord(dc);
            for (AcItem acItem : vo.getAcItems()) {
                acItem.setDcRecord(dc);
            }
            acItemRepository.saveAll(vo.getAcItems());
        }
    }


    /**
     * 审核人填写个人绩效不用审核
     * @param vo
     * @param uid
     */
    public void auditorSubmit(ApplyVO vo, int uid) {
        // 获取申请时间的时间标志, 数组大小为2, result[0]: yearmonth, result[1] week
        int[] result = utils.getTimeFlag(vo.getDate());
        int dateCode = utils.getTimeCode(vo.getDate());

        // 断言 确保一周只能向审核人提交一次
        assertException(uid, vo.getAuditorid(), vo.getId(), result[0], result[1]);

        DcRecord dc;
        if (vo.getId() == 0) {
            // 提交新的申请

            assertTimeException(vo.getDate());

            dc = DcRecord.builder()
                    .applicant(new User(uid))
                    .auditor(new User(vo.getAuditorid()))
                    .weekdate(vo.getDate())
                    .yearmonth(result[0])
                    .week(result[1])
                    .dateCode(dateCode)
                    .dvalue(vo.getDvalue())
                    .cvalue(vo.getCvalue())
                    .dc(vo.getCvalue() * vo.getDvalue())
                    .status(true)
                    .build();
            dcRecordRepository.save(dc);
        } else {
            dc = dcRecordRepository.findById(vo.getId()).get();
            dc.reApply(vo.getAuditorid(), vo.getDvalue(), vo.getDate(), result[0], result[1], dateCode);
            dc.setCvalue(vo.getCvalue());
            dc.setDc(vo.getDvalue() * vo.getCvalue());
            dc.setStatus(true);
            dcRecordRepository.save(dc);
            // 删除旧的AcItems，
            // 同时级联删除相关AcRecord !!!!!! 见AcItem实体类
            acItemRepository.deleteByDcRecord(dc);
        }

        // 持久化ac申请，并将绩效申请作为外键
        double acSum = 0;
        for (AcItem acItem : vo.getAcItems()) {
            acItem.setDcRecord(dc);
            AcRecord acRecord = acRecordRepository.save(new AcRecord(dc, acItem));
            acItem.setAcRecord(acRecord);
            acSum += acItem.getAc();
        }
        acItemRepository.saveAll(vo.getAcItems());

        // 计算总ac值
        dc.setAc(acSum);
    }

    /**
     * 判断提交的申请是否符合"一周只能向审核人提交一次"的要求
     * @param uid 申请者id
     * @param aid 审核人id
     * @param vid 提交的申请记录id
     * @param yearmonth 申请所在年月
     * @param week 申请所在周
     */
    private void assertException(int uid, int aid, int vid, int yearmonth, int week) {
        // 是否本周已经提交记录，如果有则获得它的id，否则为null
        Integer hasExist = isExist(uid, aid, yearmonth, week);
        // 如果本周已经提交记录 && 不是对已提交的记录进行修改
        if (hasExist != null && hasExist != vid) {
            // 一周只能向审核人提交一次
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "每周只能向同一个审核人提交一次申请");
        }
    }

    /**
     * 判断提交申请的时间是否符合要求
     * @param date
     */
    private void assertTimeException(LocalDate date) {
        long gap = date.until(LocalDate.now(), ChronoUnit.DAYS);
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
        List<DcRecordVO> dcRecordlist = dcRecordMapper.listDcRecordVO(uid, offset, size);
        int total = dcRecordMapper.countDcRecordByuid(uid);
        return Map.of("list", dcRecordlist, "total", total);
    }


    /**
     * 用户每周只能向同一审核人提交一个申请，判断数据库中是否已存在
     * @param uid
     * @param aid
     * @param yearmonth
     * @param week
     * @return
     */
    public Integer isExist(int uid, int aid, int yearmonth, int week) {
        return dcRecordRepository.isExist(uid, aid, yearmonth, week);
    }

}
