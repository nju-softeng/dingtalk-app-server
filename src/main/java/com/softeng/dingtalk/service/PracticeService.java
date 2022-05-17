package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Practice;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.PracticeRepository;
import com.softeng.dingtalk.repository.UserRepository;
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
        Practice practice=new Practice(userRepository.findById(uid).get(),practiceVO.getCompanyName(),practiceVO.getDepartment(),practiceVO.getStart(),practiceVO.getEnd(),practiceVO.getState());
        practiceRepository.save(practice);
    }

    public void deletePractice(int id,int uid){
        Practice practice=practiceRepository.findById(id).get();
        User user=userRepository.findById(uid).get();
        if(practice.getUser().getId()!=uid && user.getAuthority()!=2){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无删除权限！");
        }
        practiceRepository.delete(practice);
    }

    public void modifyPractice(PracticeVO practiceVO){
        if(practiceRepository.findById(practiceVO.getId()).get().getState()==1){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"审核已通过，无需修改！");
        }
        Practice practice=practiceRepository.findById(practiceVO.getId()).get();
        practice.update(practiceVO.getCompanyName(),practiceVO.getDepartment(),practiceVO.getStart(),practiceVO.getEnd(),practiceVO.getState());
        practiceRepository.save(practice);
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

    public List<Practice> getPracticeList(int uid){
        User user=userRepository.findById(uid).get();
        if(user.getName().equals(adminUserName)){
            return practiceRepository.findAllByStateEquals(0);
        } else {
            return practiceRepository.findAllByUserEquals(user);
        }
    }


}
