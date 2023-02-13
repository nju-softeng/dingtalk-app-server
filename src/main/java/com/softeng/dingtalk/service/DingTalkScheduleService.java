package com.softeng.dingtalk.service;

import com.softeng.dingtalk.api.OAApi;
import com.softeng.dingtalk.api.ScheduleApi;
import com.softeng.dingtalk.po.*;
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
        DingTalkSchedulePo dingTalkSchedulePo =new DingTalkSchedulePo(dingTalkScheduleVO.getSummary(),dingTalkScheduleVO.getStart(),dingTalkScheduleVO.getEnd(),
                dingTalkScheduleVO.isOnline(),dingTalkScheduleVO.getLocation());
        dingTalkSchedulePo.setOrganizer(userRepository.findById(uid).get());
        dingTalkSchedulePo.setDingTalkScheduleDetailList(new LinkedList<>());
        for(int id:dingTalkScheduleVO.getAttendeesIdList()){
            dingTalkSchedulePo.getDingTalkScheduleDetailList().add(new DingTalkScheduleDetailPo(userRepository.findById(id).get(), dingTalkSchedulePo));
        }
        try {
            String scheduleId=scheduleApi.creatSchedule(dingTalkSchedulePo);
            dingTalkSchedulePo.setScheduleId(scheduleId);
            dingTalkScheduleRepository.save(dingTalkSchedulePo);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void updateSchedule(DingTalkScheduleVO dingTalkScheduleVO){
        DingTalkSchedulePo dingTalkSchedulePo =dingTalkScheduleRepository.findById(dingTalkScheduleVO.getId()).get();
        dingTalkSchedulePo.update(dingTalkScheduleVO.getSummary(),dingTalkScheduleVO.getStart(),dingTalkScheduleVO.getEnd(),
                dingTalkScheduleVO.isOnline(),dingTalkScheduleVO.getLocation());
//        dingTalkSchedule.setOrganizer(userRepository.findById(dingTalkScheduleVO.getOrganizerId()).get());
        dingTalkScheduleDetailRepository.deleteAll(dingTalkSchedulePo.getDingTalkScheduleDetailList());
        dingTalkSchedulePo.setDingTalkScheduleDetailList(new LinkedList<>());
        for(int id:dingTalkScheduleVO.getAttendeesIdList()){
            dingTalkSchedulePo.getDingTalkScheduleDetailList().add(new DingTalkScheduleDetailPo(userRepository.findById(id).get(), dingTalkSchedulePo));
        }
        try {
            scheduleApi.updateSchedule(dingTalkSchedulePo);
            dingTalkScheduleRepository.save(dingTalkSchedulePo);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Map<String,Object> getScheduleList(int page, int size, int uid){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        UserPo userPo =userRepository.findById(uid).get();
        Page<DingTalkScheduleDetailPo> dingTalkScheduleDetails=dingTalkScheduleDetailRepository.getDingTalkScheduleDetailsByUserEquals(userPo,pageable);
        List<DingTalkSchedulePo> dingTalkSchedulePoList =dingTalkScheduleDetails.toList().stream().map(DingTalkScheduleDetailPo::getDingTalkSchedule).collect(Collectors.toList());
        return Map.of("list", dingTalkSchedulePoList,"total",dingTalkScheduleDetails.getTotalElements());
    }

    public void deleteSchedule(int id, int uid){
        DingTalkSchedulePo dingTalkSchedulePo =dingTalkScheduleRepository.findById(id).get();
        if(dingTalkSchedulePo.getOrganizer().getId()!=uid){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"非日程组织者，无权限删除！");
        }
        scheduleApi.deleteSchedule(dingTalkSchedulePo);
        dingTalkScheduleRepository.delete(dingTalkSchedulePo);
    }

    public void addAbsentOA(int id, int uid, AbsentOAVO absentOAVO){
        DingTalkSchedulePo dingTalkSchedulePo =dingTalkScheduleRepository.findById(id).get();
        if(absentOARepository.getAbsentOAByUserAndDingTalkSchedule(
                userRepository.findById(id).get(),
                dingTalkSchedulePo)!=null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"已提交请假审核，请删除后添加！");
        }
        AbsentOAPo absentOAPO =new AbsentOAPo(absentOAVO.getType(),absentOAVO.getStart(),absentOAVO.getEnd(),absentOAVO.getDayNum(),absentOAVO.getReason());
        absentOAPO.setUser(userRepository.findById(uid).get());
        absentOAPO.setDingTalkSchedule(dingTalkSchedulePo);
        absentOAPO.setProcessInstanceId(oaApi.createAbsentOA(absentOAPO));
        absentOARepository.save(absentOAPO);
    }



    public AbsentOAPo getAbsentOADetail(int id, int uid){
        DingTalkSchedulePo dingTalkSchedulePo =dingTalkScheduleRepository.findById(id).get();
        AbsentOAPo absentOAPO = absentOARepository.getAbsentOAByUserAndDingTalkSchedule(userRepository.findById(uid).get(), dingTalkSchedulePo);
        if(absentOAPO ==null) return null;
        if(absentOAPO.getProcessInstanceId()!=null){
            absentOAPO.setState(oaApi.getOAOutCome(absentOAPO.getProcessInstanceId()));
            absentOARepository.save(absentOAPO);
        }
        return absentOAPO;
    }

    public void deleteAbsentOA(int id,int uid){
        AbsentOAPo absentOAPO =absentOARepository.findById(id).get();
        UserPo userPo =userRepository.findById(uid).get();
        boolean isSuccess=oaApi.deleteAbsentOA(absentOAPO.getProcessInstanceId(), userPo);
        if(!isSuccess) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"删除失败");
        else absentOARepository.delete(absentOAPO);
    }

}
