package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.projection.DcRecordProjection;
import com.softeng.dingtalk.repository.AcItemRepository;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.vo.AppliedVO;
import com.softeng.dingtalk.vo.ApplingVO;
import com.softeng.dingtalk.vo.ApplyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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


    // 添加 / 更新 申请
    public DcRecord submitApplication(ApplyVO vo, int uid) {
        // 获取申请时间的时间标志, 数组大小为2, result[0]: yearmonth, result[1] week
        int[] result = utils.getTimeFlag(vo.getDate());
        int dateCode = utils.getTimeCode(vo.getDate());
        // 返回提交或更新的结果
        DcRecord res = null;
        if (vo.getId() == 0) {
            // 提交新的申请
            if (isExist(uid, vo.getAuditorid(), result[0], result[1])) {
                // 一周只能向审核人提交一次
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "每周只能向同一个审核人提交一次申请");
            }
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
            // 更新申请
            DcRecord dc = dcRecordRepository.findById(vo.getId()).get();
            dc.reApply(vo.getAuditorid(), vo.getDvalue(), vo.getDate(), result[0], result[1], dateCode);
            res = dcRecordRepository.save(dc);
            // 强制存入数据库
            dcRecordRepository.flush();
            acItemRepository.deleteByDcRecord(dc);  //删除之前的记录
            for (AcItem acItem : vo.getAcItems()) {
                acItem.setDcRecord(dc);
            }
            acItemRepository.saveAll(vo.getAcItems());
        }
        res =  dcRecordRepository.refresh(res);
        res.setAcItems(vo.getAcItems());
        return res;
    }



    // 分页获取提交过的申请
    public Map listDcRecord(int uid, int page) {
        // todo 排序！！！！
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending()); // 分页获取id,因为jpa分页是在内存中进行的，避免性能问题
        List<Integer> ids = dcRecordRepository.listIdByUid(uid, pageable);
        if (ids.size() != 0) {
            return Map.of("list", dcRecordRepository.findAllById(ids), "total", dcRecordRepository.getCountByUid(uid));
        } else {
            return Map.of("total", 0);
        }
    }


    // 获取指定用户的申请 ->  用于查看申请历史
    public Map getDcRecord(int uid, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending()); //分页对象，每页10个
        List<AppliedVO> appliedVOS =  dcRecordRepository.listByUid(uid, pageable);
        int amount = dcRecordRepository.getCountByUid(uid);
        return  Map.of("dcRecords", appliedVOS, "amount", amount);
    }


    // 每周1个用户只能向同一审核人提交一个申请，判断数据库中是否已存在
    public boolean isExist(int uid, int aid, int yearmonth, int week) {
        return dcRecordRepository.isExist(uid, aid, yearmonth, week) != 0 ? true : false;
    }



}
