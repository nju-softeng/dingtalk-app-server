package com.softeng.dingtalk.service;

import com.softeng.dingtalk.po.PracticePo;
import com.softeng.dingtalk.po.UserPo;
import com.softeng.dingtalk.dao.repository.PracticeRepository;
import com.softeng.dingtalk.dao.repository.UserRepository;
import com.softeng.dingtalk.vo.PracticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@Slf4j
public class PracticeService {
    public final static String adminUserName="Jason";
    @Autowired
    PracticeRepository practiceRepository;
    @Autowired
    UserRepository userRepository;
    public void addPractice(PracticeVO practiceVO,int uid){
        PracticePo practicePo =new PracticePo(userRepository.findById(uid).get(),practiceVO.getCompanyName(),practiceVO.getDepartment(),practiceVO.getStart(),practiceVO.getEnd(),practiceVO.getState());
        practiceRepository.save(practicePo);
    }

    // todo-权限相关（替换原本的authority）
    public void deletePractice(int id,int uid){
        PracticePo practicePo =practiceRepository.findById(id).get();
        UserPo userPo =userRepository.findById(uid).get();
        if(practicePo.getUser().getId()!=uid){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无删除权限！");
        }
        practiceRepository.delete(practicePo);
    }

    public void modifyPractice(PracticeVO practiceVO){
        if(practiceRepository.findById(practiceVO.getId()).get().getState()==1){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"审核已通过，无需修改！");
        }
        PracticePo practicePo =practiceRepository.findById(practiceVO.getId()).get();
        practicePo.update(practiceVO.getCompanyName(),practiceVO.getDepartment(),practiceVO.getStart(),practiceVO.getEnd(),practiceVO.getState());
        practiceRepository.save(practicePo);
    }

//    public void audit(int id, int uid, boolean isPass){
//        Practice practice=practiceRepository.findById(id).get();
//        if(isPass){
//            practice.setState(1);
//        } else {
//            practice.setState(-1);
//        }
//        practiceRepository.save(practice);
//    }

    public List<PracticePo> getPracticeList(int uid){
        UserPo userPo =userRepository.findById(uid).get();
        if(userPo.getName().equals(adminUserName)){
            return practiceRepository.findAllByStateEquals(0);
        } else {
            return practiceRepository.findAllByUserEquals(userPo);
        }
    }


}
