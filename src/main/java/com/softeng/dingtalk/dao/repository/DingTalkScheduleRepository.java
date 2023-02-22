package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.DingTalkSchedule;
import com.softeng.dingtalk.po_entity.DingTalkScheduleDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DingTalkScheduleRepository extends CustomizedRepository<DingTalkSchedule,Integer>{
    List<DingTalkSchedule> getDingTalkSchedulesByAcCalculatedFalse();
    List<DingTalkSchedule> getDingTalkSchedulesByDingTalkScheduleDetailListContains(DingTalkScheduleDetail dingTalkScheduleDetail);
}