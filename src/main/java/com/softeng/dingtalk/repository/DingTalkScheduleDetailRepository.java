package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.DingTalkSchedule;
import com.softeng.dingtalk.entity.DingTalkScheduleDetail;
import com.softeng.dingtalk.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface DingTalkScheduleDetailRepository extends CustomizedRepository<DingTalkScheduleDetail,Integer>{
    DingTalkScheduleDetail getDingTalkScheduleDetailByUserEquals(User user);
    DingTalkScheduleDetail getDingTalkScheduleDetailByUserAndDingTalkSchedule(User user, DingTalkSchedule dingTalkSchedule);
}
