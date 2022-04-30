package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.DingTalkSchedule;
import com.softeng.dingtalk.entity.DingTalkScheduleDetail;
import com.softeng.dingtalk.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DingTalkScheduleRepository extends CustomizedRepository<DingTalkSchedule,Integer>{
    List<DingTalkSchedule> getDingTalkSchedulesByIsAcCalculatedFalse();
    List<DingTalkSchedule> getDingTalkSchedulesByDingTalkScheduleDetailListContains(DingTalkScheduleDetail dingTalkScheduleDetail);
}
