package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.mapper.DcRecordMapper;
import com.softeng.dingtalk.repository.AcItemRepository;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.vo.ApplyVO;
import com.softeng.dingtalk.vo.DcRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    /**
     * 添加 / 更新 申请
     * @param vo
     * @param uid
     * @return
     */
    public DcRecord submitApplication(ApplyVO vo, int uid) {
        // 获取申请时间的时间标志, 数组大小为2, result[0]: yearmonth, result[1] week
        int[] result = utils.getTimeFlag(vo.getDate());
        int dateCode = utils.getTimeCode(vo.getDate());
        // 返回提交或更新的结果
        DcRecord res = null;
        // 是否本周已经提交记录，如果有则获得它的id，否则为null
        Integer hasExist = isExist(uid, vo.getAuditorid(), result[0], result[1]);
        // 如果本周已经提交记录 && 不是对已提交的记录进行修改
        if (hasExist != null && hasExist != vo.getId()) {
            // 一周只能向审核人提交一次
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "每周只能向同一个审核人提交一次申请");
        }
        if (vo.getId() == 0) {
            // 提交新的申请
            DcRecord dc = DcRecord.builder().applicant(new User(uid)).auditor(new User(vo.getAuditorid()))
                    .dvalue(vo.getDvalue()).ac(vo.getAc()).weekdate(vo.getDate()).yearmonth(result[0])
                    .week(result[1]).dateCode(dateCode).build();
            res = dcRecordRepository.save(dc);
            for (AcItem acItem : vo.getAcItems()) {
                // 持久化ac申请，并将绩效申请作为外键
                acItem.setDcRecord(dc);
            }
            acItemRepository.saveAll(vo.getAcItems());
        } else {
            DcRecord dc = dcRecordRepository.findById(vo.getId()).get();
            dc.reApply(vo.getAuditorid(), vo.getDvalue(), vo.getDate(), result[0], result[1], dateCode);
            res = dcRecordRepository.save(dc);
            // 强制存入数据库
            dcRecordRepository.flush();
            //删除之前的记录
            acItemRepository.deleteByDcRecord(dc);
            for (AcItem acItem : vo.getAcItems()) {
                acItem.setDcRecord(dc);
            }
            acItemRepository.saveAll(vo.getAcItems());
        }
        res =  dcRecordRepository.refresh(res);
        res.setAcItems(vo.getAcItems());
        return res;
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
