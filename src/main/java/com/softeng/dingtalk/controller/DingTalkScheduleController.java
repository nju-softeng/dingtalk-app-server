package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.service.DingTalkScheduleService;
import com.softeng.dingtalk.vo.DingTalkScheduleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class DingTalkScheduleController {
    @Autowired
    DingTalkScheduleService dingTalkScheduleService;
    @PostMapping("/schedule")
    public void addSchedule(DingTalkScheduleVO dingTalkScheduleVO){
        if(dingTalkScheduleVO.getId()==null) {
            dingTalkScheduleService.addSchedule(dingTalkScheduleVO);
        } else {
            dingTalkScheduleService.updateSchedule(dingTalkScheduleVO);
        }
    }

    @GetMapping("/schedule/page/{page}/{size}")
    public void getScheduleList(@PathVariable int page,@PathVariable int size,@RequestAttribute int uid){

    }
}
