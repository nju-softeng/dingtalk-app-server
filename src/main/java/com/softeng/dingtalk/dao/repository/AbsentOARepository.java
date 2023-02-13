package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.AbsentOAPo;
import com.softeng.dingtalk.po.DingTalkSchedulePo;
import com.softeng.dingtalk.po.UserPo;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsentOARepository extends CustomizedRepository<AbsentOAPo,Integer>{
    AbsentOAPo getAbsentOAByUserAndDingTalkSchedule(UserPo userPo, DingTalkSchedulePo dingTalkSchedulePo);
}
