package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.AbsentOA;
import com.softeng.dingtalk.po_entity.DingTalkSchedule;
import com.softeng.dingtalk.po_entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsentOARepository extends CustomizedRepository<AbsentOA,Integer>{
    AbsentOA getAbsentOAByUserAndDingTalkSchedule(User user, DingTalkSchedule dingTalkSchedule);
}
