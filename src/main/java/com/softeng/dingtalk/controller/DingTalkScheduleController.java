package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.AbsentOA;
import com.softeng.dingtalk.entity.DingTalkSchedule;
import com.softeng.dingtalk.service.DingTalkScheduleService;
import com.softeng.dingtalk.vo.AbsentOAVO;
import com.softeng.dingtalk.vo.DingTalkScheduleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class DingTalkScheduleController {
    @Autowired
    DingTalkScheduleService dingTalkScheduleService;
    @PostMapping("/schedule")
    public void addSchedule(DingTalkScheduleVO dingTalkScheduleVO,@RequestAttribute int uid){
        if(dingTalkScheduleVO.getId()==null) {
            dingTalkScheduleService.addSchedule(dingTalkScheduleVO,uid);
        } else {
            dingTalkScheduleService.updateSchedule(dingTalkScheduleVO);
        }
    }

    @DeleteMapping("/schedule/{id}")
    public void deleteSchedule(@PathVariable int id,@RequestAttribute int uid){
        dingTalkScheduleService.deleteSchedule(id,uid);
    }

    @GetMapping("/schedule/page/{page}/{size}")
    public List<DingTalkSchedule> getScheduleList(@PathVariable int page, @PathVariable int size, @RequestAttribute int uid){
        return dingTalkScheduleService.getScheduleList(uid);
    }

    @PostMapping("/schedule/{id}/absentOA")
    public void addAbsentOA(@PathVariable int id, @RequestAttribute int uid, @RequestBody AbsentOAVO absentOAVO){
        dingTalkScheduleService.addAbsentOA(id,uid,absentOAVO);
    }

    @GetMapping("/schedule/{id}/absentOA")
    public AbsentOA getAbsentOADetail(@PathVariable int id, @RequestAttribute int uid){
        return dingTalkScheduleService.getAbsentOADetail(id,uid);
    }

    @DeleteMapping("absentOA/{id}")
    public void deleteAbsentOA(@PathVariable int id, @RequestAttribute int uid){
        dingTalkScheduleService.deleteAbsentOA(id,uid);
    }


}
