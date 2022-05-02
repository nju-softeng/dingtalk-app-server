package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.DingTalkSchedule;
import com.softeng.dingtalk.entity.DingTalkScheduleDetail;
import com.softeng.dingtalk.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DingTalkScheduleDetailRepository extends CustomizedRepository<DingTalkScheduleDetail,Integer>{
    List<DingTalkScheduleDetail> getDingTalkScheduleDetailsByUserEquals(User user);
    DingTalkScheduleDetail getDingTalkScheduleDetailByUserAndDingTalkSchedule(User user, DingTalkSchedule dingTalkSchedule);
}
