package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.dingApi.OAApi;
import com.softeng.dingtalk.component.dingApi.ScheduleApi;
import com.softeng.dingtalk.po_entity.*;
import com.softeng.dingtalk.dao.repository.AbsentOARepository;
import com.softeng.dingtalk.dao.repository.DingTalkScheduleDetailRepository;
import com.softeng.dingtalk.dao.repository.DingTalkScheduleRepository;
import com.softeng.dingtalk.dao.repository.UserRepository;
import com.softeng.dingtalk.vo.AbsentOAVO;
import com.softeng.dingtalk.vo.DingTalkScheduleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DingTalkScheduleService {
    @Autowired
    ScheduleApi scheduleApi;
    @Autowired
    OAApi oaApi;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DingTalkScheduleRepository dingTalkScheduleRepository;
    @Autowired
    DingTalkScheduleDetailRepository dingTalkScheduleDetailRepository;
    @Autowired
    AbsentOARepository absentOARepository;
    public void addSchedule(DingTalkScheduleVO dingTalkScheduleVO,int uid){
        DingTalkSchedule dingTalkSchedule =new DingTalkSchedule(dingTalkScheduleVO.getSummary(),dingTalkScheduleVO.getStart(),dingTalkScheduleVO.getEnd(),
                dingTalkScheduleVO.isOnline(),dingTalkScheduleVO.getLocation());
        dingTalkSchedule.setOrganizer(userRepository.findById(uid).get());
        dingTalkSchedule.setDingTalkScheduleDetailList(new LinkedList<>());
        for(int id:dingTalkScheduleVO.getAttendeesIdList()){
            dingTalkSchedule.getDingTalkScheduleDetailList().add(new DingTalkScheduleDetail(userRepository.findById(id).get(), dingTalkSchedule));
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
        DingTalkSchedule dingTalkSchedule =dingTalkScheduleRepository.findById(dingTalkScheduleVO.getId()).get();
        dingTalkSchedule.update(dingTalkScheduleVO.getSummary(),dingTalkScheduleVO.getStart(),dingTalkScheduleVO.getEnd(),
                dingTalkScheduleVO.isOnline(),dingTalkScheduleVO.getLocation());
//        dingTalkSchedule.setOrganizer(userRepository.findById(dingTalkScheduleVO.getOrganizerId()).get());
        dingTalkScheduleDetailRepository.deleteAll(dingTalkSchedule.getDingTalkScheduleDetailList());
        dingTalkSchedule.setDingTalkScheduleDetailList(new LinkedList<>());
        for(int id:dingTalkScheduleVO.getAttendeesIdList()){
            dingTalkSchedule.getDingTalkScheduleDetailList().add(new DingTalkScheduleDetail(userRepository.findById(id).get(), dingTalkSchedule));
        }
        try {
            scheduleApi.updateSchedule(dingTalkSchedule);
            dingTalkScheduleRepository.save(dingTalkSchedule);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Map<String,Object> getScheduleList(int page, int size, int uid){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
//        User user =userRepository.findById(uid).get();
//        Page<DingTalkScheduleDetail> dingTalkScheduleDetails=dingTalkScheduleDetailRepository.getDingTalkScheduleDetailsByUserEquals(user,pageable);
//        List<DingTalkSchedule> dingTalkScheduleList =dingTalkScheduleDetails.toList().stream().map(DingTalkScheduleDetail::getDingTalkSchedule).collect(Collectors.toList());
//        return Map.of("list", dingTalkScheduleList,"total",dingTalkScheduleDetails.getTotalElements());
        Page<DingTalkSchedule> dingTalkSchedulePage = dingTalkScheduleRepository.findAll(pageable);
        return Map.of("list", dingTalkSchedulePage.toList(), "total", dingTalkSchedulePage.getTotalElements());
    }

    public void deleteSchedule(int id, int uid){
        DingTalkSchedule dingTalkSchedule =dingTalkScheduleRepository.findById(id).get();
        if(dingTalkSchedule.getOrganizer().getId()!=uid){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"非日程组织者，无权限删除！");
        }
        scheduleApi.deleteSchedule(dingTalkSchedule);
        dingTalkScheduleRepository.delete(dingTalkSchedule);
    }

    public void addAbsentOA(int id, int uid, AbsentOAVO absentOAVO){
        DingTalkSchedule dingTalkSchedule =dingTalkScheduleRepository.findById(id).get();
        if(absentOARepository.getAbsentOAByUserAndDingTalkSchedule(
                userRepository.findById(id).get(),
                dingTalkSchedule)!=null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"已提交请假审核，请删除后添加！");
        }
        AbsentOA absentOAPO =new AbsentOA(absentOAVO.getType(),absentOAVO.getStart(),absentOAVO.getEnd(),absentOAVO.getDayNum(),absentOAVO.getReason());
        absentOAPO.setUser(userRepository.findById(uid).get());
        absentOAPO.setDingTalkSchedule(dingTalkSchedule);
        absentOAPO.setProcessInstanceId(oaApi.createAbsentOA(absentOAPO));
        absentOARepository.save(absentOAPO);
    }



    public AbsentOA getAbsentOADetail(int id, int uid){
        DingTalkSchedule dingTalkSchedule =dingTalkScheduleRepository.findById(id).get();
        AbsentOA absentOAPO = absentOARepository.getAbsentOAByUserAndDingTalkSchedule(userRepository.findById(uid).get(), dingTalkSchedule);
        if(absentOAPO ==null) return null;
        if(absentOAPO.getProcessInstanceId()!=null){
            absentOAPO.setState(oaApi.getOAOutCome(absentOAPO.getProcessInstanceId()));
            absentOARepository.save(absentOAPO);
        }
        return absentOAPO;
    }

    public void deleteAbsentOA(int id,int uid){
        AbsentOA absentOAPO =absentOARepository.findById(id).get();
        User user =userRepository.findById(uid).get();
        boolean isSuccess=oaApi.deleteAbsentOA(absentOAPO.getProcessInstanceId(), user);
        if(!isSuccess) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"删除失败");
        else absentOARepository.delete(absentOAPO);
    }

}
