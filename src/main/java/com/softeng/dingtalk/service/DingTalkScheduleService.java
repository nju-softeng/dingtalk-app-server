package com.softeng.dingtalk.service;

import com.softeng.dingtalk.api.ScheduleApi;
import com.softeng.dingtalk.entity.DingTalkSchedule;
import com.softeng.dingtalk.entity.DingTalkScheduleDetail;
import com.softeng.dingtalk.repository.DingTalkScheduleDetailRepository;
import com.softeng.dingtalk.repository.DingTalkScheduleRepository;
import com.softeng.dingtalk.repository.UserRepository;
import com.softeng.dingtalk.vo.DingTalkScheduleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;

@Service
@Transactional
@Slf4j
public class DingTalkScheduleService {
    @Autowired
    ScheduleApi scheduleApi;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DingTalkScheduleRepository dingTalkScheduleRepository;
    @Autowired
    DingTalkScheduleDetailRepository dingTalkScheduleDetailRepository;
    public void addSchedule(DingTalkScheduleVO dingTalkScheduleVO){
        DingTalkSchedule dingTalkSchedule=new DingTalkSchedule(dingTalkScheduleVO.getSummary(),dingTalkScheduleVO.getStart(),dingTalkScheduleVO.getEnd(),
                dingTalkScheduleVO.isOnline(),dingTalkScheduleVO.getLocation());
        dingTalkSchedule.setOrganizer(userRepository.findById(dingTalkScheduleVO.getOrganizerId()).get());
        dingTalkSchedule.setDingTalkScheduleDetailList(new LinkedList<>());
        for(int id:dingTalkScheduleVO.getAttendeesIdList()){
            dingTalkSchedule.getDingTalkScheduleDetailList().add(new DingTalkScheduleDetail(userRepository.findById(id).get(),dingTalkSchedule));
        }
        try {
            String scheduleId=scheduleApi.creatSchedule(dingTalkSchedule);
            dingTalkSchedule.setScheduleId(scheduleId);
            dingTalkScheduleRepository.save(dingTalkSchedule);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void updateSchedule(DingTalkScheduleVO dingTalkScheduleVO){
        DingTalkSchedule dingTalkSchedule=dingTalkScheduleRepository.findById(dingTalkScheduleVO.getId()).get();
        dingTalkSchedule.update(dingTalkScheduleVO.getSummary(),dingTalkScheduleVO.getStart(),dingTalkScheduleVO.getEnd(),
                dingTalkScheduleVO.isOnline(),dingTalkScheduleVO.getLocation());
        dingTalkSchedule.setOrganizer(userRepository.findById(dingTalkScheduleVO.getOrganizerId()).get());
        dingTalkSchedule.setDingTalkScheduleDetailList(new LinkedList<>());
        dingTalkScheduleDetailRepository.deleteAll(dingTalkSchedule.getDingTalkScheduleDetailList());
        for(int id:dingTalkScheduleVO.getAttendeesIdList()){
            dingTalkSchedule.getDingTalkScheduleDetailList().add(new DingTalkScheduleDetail(userRepository.findById(id).get(),dingTalkSchedule));
        }
        try {
            scheduleApi.updateSchedule(dingTalkSchedule);
            dingTalkScheduleRepository.save(dingTalkSchedule);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
