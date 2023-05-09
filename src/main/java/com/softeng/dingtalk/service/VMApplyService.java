package com.softeng.dingtalk.service;

import com.softeng.dingtalk.aspect.AccessPermission;
import com.softeng.dingtalk.component.convertor.VMApplyConvertor;
import com.softeng.dingtalk.dto.req.VMApplyReq;
import com.softeng.dingtalk.dto.resp.VMApplyResp;
import com.softeng.dingtalk.enums.PermissionEnum;
import com.softeng.dingtalk.po_entity.User;
import com.softeng.dingtalk.po_entity.VMApply;
import com.softeng.dingtalk.dao.repository.UserRepository;
import com.softeng.dingtalk.dao.repository.VMApplyRepository;
import com.softeng.dingtalk.utils.StreamUtils;
import com.softeng.dingtalk.vo.VMApplyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
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
    @Resource
    VMApplyConvertor vmApplyConvertor;

    public void addVMApply(VMApplyVO vmApplyVO,int uid){
        User user =userRepository.findById(uid).get();
        vmApplyVO.setUser(user);
        VMApply vmApply =new VMApply(vmApplyVO.getUser(), vmApplyVO.getProjectTeam(), vmApplyVO.getSubject(),
                vmApplyVO.getEmail(), vmApplyVO.getStart(), vmApplyVO.getEnd(), vmApplyVO.getPurpose(), vmApplyVO.getCoreNum(),
                vmApplyVO.getMemory(), vmApplyVO.getCapacity(), vmApplyVO.getOperationSystem(), vmApplyVO.getApplyDate());
        vmApply.setState(0);
        vmApplyRepository.save(vmApply);
    }
    public void updateVMApply(VMApplyVO vmApplyVO,int uid){
        User user =userRepository.findById(uid).get();
        VMApply vmApply =vmApplyRepository.findById(vmApplyVO.getId()).get();
        if(!vmApply.getUser().equals(user))
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无修改权限！");
        vmApply.update(vmApplyVO.getProjectTeam(), vmApplyVO.getSubject(),
                vmApplyVO.getEmail(), vmApplyVO.getStart(), vmApplyVO.getEnd(), vmApplyVO.getPurpose(), vmApplyVO.getCoreNum(),
                vmApplyVO.getMemory(), vmApplyVO.getCapacity(), vmApplyVO.getOperationSystem());
        vmApplyRepository.save(vmApply);
    }

    public Map<String,Object> getVMApplyList(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size, Sort.by("id").descending());
        Page<VMApply> vmApplies=vmApplyRepository.findAll(pageable);
        List<VMApply> vmApplyList =vmApplies.toList();
        return Map.of("list", vmApplyList,"total",vmApplies.getTotalElements());
    }

    public List<VMApply> getAuditingVMApplyList(){
        return vmApplyRepository.findAllByStateEquals(0);
    }

    public List<VMApply> getUserVMApplyList(int uid){
        User user =userRepository.findById(uid).get();
        return vmApplyRepository.findAllByUserEquals(user);
    }

    @AccessPermission(PermissionEnum.REVIEW_VM_DEVICE_APPLICATION)
    public void setVMApplyState(int id, boolean isPass,int uid){
        User user =userRepository.findById(uid).get();
//        if(userPo.getAuthority()!=2) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无审核权限！");
        VMApply vmApply =vmApplyRepository.findById(id).get();
        if(isPass) vmApply.setState(1);
        else vmApply.setState(-1);
    }

    @AccessPermission(PermissionEnum.REVIEW_VM_DEVICE_APPLICATION)
    public void deleteVMApply(int id, int uid){
        User user =userRepository.findById(uid).get();
        VMApply vmApply =vmApplyRepository.findById(id).get();
//        if(userPo.getAuthority()!=2 && !vmApply.getUser().equals(userPo))
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无删除权限！");
        vmApplyRepository.delete(vmApply);
    }

    public Map<String, Object> queryVMApplyList(int page, int size, VMApplyReq vmApplyReq) {
        Pageable pageable = PageRequest.of(page - 1,size, Sort.by("id").descending());
        Specification<VMApply> vmApplySpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(vmApplyReq.getState() != -2)
                predicates.add(criteriaBuilder.equal(root.get("state"), vmApplyReq.getState()));
            if(!"".equals(vmApplyReq.getProjectTeam()))
                predicates.add(criteriaBuilder.equal(root.get("projectTeam"), vmApplyReq.getProjectTeam()));
            if(vmApplyReq.getUserId() != 0)
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), vmApplyReq.getUserId()));
            Predicate[] arr = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(arr));
        });
        Page<VMApply> vmApplyPage = vmApplyRepository.findAll(vmApplySpecification, pageable);
        List<VMApplyResp> vmApplyReqList = StreamUtils.map(vmApplyPage.toList(), vmApply -> vmApplyConvertor.entity_PO2Resp(vmApply));
        return Map.of("list", vmApplyReqList, "total", vmApplyPage.getTotalElements());
    }

}
