package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Reimbursement;
import com.softeng.dingtalk.repository.ReimbursementRepository;
import com.softeng.dingtalk.vo.ReimbursementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional
@Slf4j
public class ReimburseService {
    @Autowired
    ReimbursementRepository reimbursementRepository;
    public void addReimbursement(ReimbursementVO reimbursementVO){
        Reimbursement reimbursement=new Reimbursement(reimbursementVO.getType(),reimbursementVO.getPath());
        reimbursementRepository.save(reimbursement);
    }

    public void updateReimbursement(ReimbursementVO reimbursementVO){
        Reimbursement reimbursement=reimbursementRepository.findById(reimbursementVO.getId()).get();
        reimbursement.update(reimbursementVO.getType(),reimbursement.getPath());
        reimbursementRepository.save(reimbursement);
    }

    public void setState(int id,int state){
        Reimbursement reimbursement=reimbursementRepository.findById(id).get();
        reimbursement.setState(state);
        reimbursementRepository.save(reimbursement);
    }
}
