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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
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


    // 添加申请
    public void addApplication(DcRecord dcRecord, List<AcItem> acItems) {
        dcRecordRepository.save(dcRecord);
        for (int i = 0; i < acItems.size(); i++) { // 持久化ac申请，并将绩效申请作为外键
            acItems.get(i).setDcRecord(dcRecord);
        }
        acItemRepository.saveAll(acItems);
    }


    // 更新申请
    public void updateApplication(ApplingVO applingVO) {
        int[] result = utils.getTimeFlag(applingVO.getDate()); //数组大小为2，result[0]: yearmonth, result[1] week
        DcRecord dc = dcRecordRepository.findById(applingVO.getId()).get();
        dc.reApply(applingVO.getAuditorid(), applingVO.getDvalue(), applingVO.getDate(), result[0], result[1]);
        acItemRepository.deleteByDcRecord(dc);  //删除之前的记录
        for (AcItem acItem : applingVO.getAcItems()) {
            acItem.setDcRecord(dc);
        }
        acItemRepository.saveAll(applingVO.getAcItems());
    }


    // 分页获取提交过的申请
    public Map listDcRecord(int uid, int page) {
        // todo 排序！！！！
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").ascending()); // 分页获取id,因为jpa分页是在内存中进行的，避免性能问题
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


    //todo  去掉该函数
    public List<AcItem> listAcItemBydcid(int id) {
        return acItemRepository.findAllByDcRecordID(id);
    }


}
