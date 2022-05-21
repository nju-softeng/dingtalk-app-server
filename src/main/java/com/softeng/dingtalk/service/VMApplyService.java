package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.entity.VMApply;
import com.softeng.dingtalk.repository.UserRepository;
import com.softeng.dingtalk.repository.VMApplyRepository;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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
        User user=userRepository.findById(uid).get();
        vmApplyVO.setUser(user);
        VMApply vmApply =new VMApply(vmApplyVO.getUser(), vmApplyVO.getProjectTeam(), vmApplyVO.getSubject(),
                vmApplyVO.getEmail(), vmApplyVO.getStart(), vmApplyVO.getEnd(), vmApplyVO.getPurpose(), vmApplyVO.getCoreNum(),
                vmApplyVO.getMemory(), vmApplyVO.getCapacity(), vmApplyVO.getOperationSystem(), vmApplyVO.getApplyDate());
        vmApplyRepository.save(vmApply);
    }
    public void updateVMApply(VMApplyVO vmApplyVO,int uid){
        User user=userRepository.findById(uid).get();
        VMApply vmApply=vmApplyRepository.findById(vmApplyVO.getId()).get();
        if(user.getAuthority()!=2 && !vmApply.getUser().equals(user))
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无修改权限！");
        vmApply.update(vmApplyVO.getProjectTeam(), vmApplyVO.getSubject(),
                vmApplyVO.getEmail(), vmApplyVO.getStart(), vmApplyVO.getEnd(), vmApplyVO.getPurpose(), vmApplyVO.getCoreNum(),
                vmApplyVO.getMemory(), vmApplyVO.getCapacity(), vmApplyVO.getOperationSystem());
        vmApplyRepository.save(vmApply);
    }

    public Map<String,Object> getVMApplyList(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<VMApply> vmApplies=vmApplyRepository.findAll(pageable);
        List<VMApply> vmApplyList=vmApplies.toList();
        return Map.of("list",vmApplyList,"total",vmApplies.getTotalElements());
    }

    public List<VMApply> getAuditingVMApplyList(){
        return vmApplyRepository.findAllByStateEquals(0);
    }

    public List<VMApply> getUserVMApplyList(int uid){
        User user=userRepository.findById(uid).get();
        return vmApplyRepository.findAllByUserEquals(user);
    }

    public void setVMApplyState(int id, boolean isPass,int uid){
        User user=userRepository.findById(uid).get();
        if(user.getAuthority()!=2) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无审核权限！");
        VMApply vmApply=vmApplyRepository.findById(id).get();
        if(isPass) vmApply.setState(1);
        else vmApply.setState(-1);
    }

    public void deleteVMApply(int id, int uid){
        User user=userRepository.findById(uid).get();
        VMApply vmApply=vmApplyRepository.findById(id).get();
        if(user.getAuthority()!=2 && !vmApply.getUser().equals(user))
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无删除权限！");
        vmApplyRepository.delete(vmApply);
    }

}
