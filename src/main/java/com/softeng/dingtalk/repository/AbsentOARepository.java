package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.AbsentOA;
import com.softeng.dingtalk.entity.DingTalkSchedule;
import com.softeng.dingtalk.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsentOARepository extends CustomizedRepository<AbsentOA,Integer>{
    AbsentOA getAbsentOAByUserAndDingTalkSchedule(User user, DingTalkSchedule dingTalkSchedule);
}
