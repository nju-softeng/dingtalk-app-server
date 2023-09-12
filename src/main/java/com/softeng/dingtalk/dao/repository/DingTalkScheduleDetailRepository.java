package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.entity.DingTalkSchedule;
import com.softeng.dingtalk.entity.DingTalkScheduleDetail;
import com.softeng.dingtalk.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface DingTalkScheduleDetailRepository extends CustomizedRepository<DingTalkScheduleDetail,Integer>{
    Page<DingTalkScheduleDetail> getDingTalkScheduleDetailsByUserEquals(User user, Pageable pageable);
    DingTalkScheduleDetail getDingTalkScheduleDetailByUserAndDingTalkSchedule(User user, DingTalkSchedule dingTalkSchedule);
}
