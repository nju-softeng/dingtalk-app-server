package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.repository.AcItemRepository;
import com.softeng.dingtalk.repository.DcRecordRepository;
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




    /**
     * 每周1个用户只能向同一审核人提交一个申请，判断数据库中是否已存在
     * @param uid 用户ID
     * @param aid 审核人ID
     * @param yearmonth,week
     * @return boolean
     * @Date 8:12 PM 12/30/2019
     **/
    public boolean isExist(int uid, int aid, int yearmonth, int week) {
        //todo 注意测试
        return dcRecordRepository.isExist(uid, aid, yearmonth, week) != 0 ? true : false;
    }

    /**
     * 添加申请 ->  用户提交一个周绩效申请（包括DC和AC）
     * @param dcRecord, acItems
     * @return void
     * @Date 8:22 PM 12/30/2019
     **/
    public void addApplication(DcRecord dcRecord, List<AcItem> acItems) {
        dcRecordRepository.save(dcRecord);
        for (int i = 0; i < acItems.size(); i++) { // 持久化ac申请，并将绩效申请作为外键
            acItems.get(i).setDcRecord(dcRecord);
        }
        acItemRepository.saveAll(acItems);
    }

    /**
     * 获取指定用户的申请 ->  用于查看申请历史
     * @param uid, page
     * @return java.util.List<com.softeng.dingtalk.entity.D cRecord>
     * @Date 8:22 PM 12/30/2019
     **/
    public Map getDcRecord(int uid, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending()); //分页对象，每页10个
        List<DcRecord> dcRecords =  dcRecordRepository.listByUid(uid, pageable);
        int amount = dcRecordRepository.getCountByUid(uid);
        return  Map.of("dcRecords", dcRecords, "amount", amount);
    }





}
