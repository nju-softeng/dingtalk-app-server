package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.DingTalkSchedulePo;
import com.softeng.dingtalk.po.DingTalkScheduleDetailPo;
import com.softeng.dingtalk.po.UserPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface DingTalkScheduleDetailRepository extends CustomizedRepository<DingTalkScheduleDetailPo,Integer>{
    Page<DingTalkScheduleDetailPo> getDingTalkScheduleDetailsByUserEquals(UserPo userPo, Pageable pageable);
    DingTalkScheduleDetailPo getDingTalkScheduleDetailByUserAndDingTalkSchedule(UserPo userPo, DingTalkSchedulePo dingTalkSchedulePo);
}
