package com.softeng.dingtalk.service;

import com.softeng.dingtalk.po.UserPo;
import com.softeng.dingtalk.po.VMApplyPo;
import com.softeng.dingtalk.dao.repository.UserRepository;
import com.softeng.dingtalk.dao.repository.VMApplyRepository;
import com.softeng.dingtalk.vo.VMApplyVO;
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

import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class VMApplyService {
    @Autowired
    VMApplyRepository vmApplyRepository;
    @Autowired
    UserRepository userRepository;
    public void addVMApply(VMApplyVO vmApplyVO,int uid){
        UserPo userPo =userRepository.findById(uid).get();
        vmApplyVO.setUserPo(userPo);
        VMApplyPo vmApplyPo =new VMApplyPo(vmApplyVO.getUserPo(), vmApplyVO.getProjectTeam(), vmApplyVO.getSubject(),
                vmApplyVO.getEmail(), vmApplyVO.getStart(), vmApplyVO.getEnd(), vmApplyVO.getPurpose(), vmApplyVO.getCoreNum(),
                vmApplyVO.getMemory(), vmApplyVO.getCapacity(), vmApplyVO.getOperationSystem(), vmApplyVO.getApplyDate());
        vmApplyRepository.save(vmApplyPo);
    }
    public void updateVMApply(VMApplyVO vmApplyVO,int uid){
        UserPo userPo =userRepository.findById(uid).get();
        VMApplyPo vmApplyPo =vmApplyRepository.findById(vmApplyVO.getId()).get();
        if(!vmApplyPo.getUser().equals(userPo))
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无修改权限！");
        vmApplyPo.update(vmApplyVO.getProjectTeam(), vmApplyVO.getSubject(),
                vmApplyVO.getEmail(), vmApplyVO.getStart(), vmApplyVO.getEnd(), vmApplyVO.getPurpose(), vmApplyVO.getCoreNum(),
                vmApplyVO.getMemory(), vmApplyVO.getCapacity(), vmApplyVO.getOperationSystem());
        vmApplyRepository.save(vmApplyPo);
    }

    public Map<String,Object> getVMApplyList(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<VMApplyPo> vmApplies=vmApplyRepository.findAll(pageable);
        List<VMApplyPo> vmApplyPoList =vmApplies.toList();
        return Map.of("list", vmApplyPoList,"total",vmApplies.getTotalElements());
    }

    public List<VMApplyPo> getAuditingVMApplyList(){
        return vmApplyRepository.findAllByStateEquals(0);
    }

    public List<VMApplyPo> getUserVMApplyList(int uid){
        UserPo userPo =userRepository.findById(uid).get();
        return vmApplyRepository.findAllByUserEquals(userPo);
    }

//    todo-权限相关
    public void setVMApplyState(int id, boolean isPass,int uid){
        UserPo userPo =userRepository.findById(uid).get();
//        if(userPo.getAuthority()!=2) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无审核权限！");
        VMApplyPo vmApplyPo =vmApplyRepository.findById(id).get();
        if(isPass) vmApplyPo.setState(1);
        else vmApplyPo.setState(-1);
    }

    //    todo-权限相关
    public void deleteVMApply(int id, int uid){
        UserPo userPo =userRepository.findById(uid).get();
        VMApplyPo vmApplyPo =vmApplyRepository.findById(id).get();
//        if(userPo.getAuthority()!=2 && !vmApplyPo.getUser().equals(userPo))
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无删除权限！");
        vmApplyRepository.delete(vmApplyPo);
    }

}
