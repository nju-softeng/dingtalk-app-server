package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.DingTalkSchedulePo;
import com.softeng.dingtalk.po.DingTalkScheduleDetailPo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DingTalkScheduleRepository extends CustomizedRepository<DingTalkSchedulePo,Integer>{
    List<DingTalkSchedulePo> getDingTalkSchedulesByAcCalculatedFalse();
    List<DingTalkSchedulePo> getDingTalkSchedulesByDingTalkScheduleDetailListContains(DingTalkScheduleDetailPo dingTalkScheduleDetail);
}
