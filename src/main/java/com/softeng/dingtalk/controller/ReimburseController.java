package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.Reimbursement;
import com.softeng.dingtalk.service.ReimburseService;
import com.softeng.dingtalk.vo.ReimbursementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ReimburseController {
    @Autowired
    ReimburseService reimburseService;
    @PostMapping("/reimburse")
    public void addReimbursement(@RequestBody ReimbursementVO reimbursementVO){
        if(reimbursementVO.getId()==null){
            reimburseService.addReimbursement(reimbursementVO);
        } else {
            reimburseService.updateReimbursement(reimbursementVO);
        }
    }

    @PutMapping("/reimburse/{id}/state/{state}")
    public void setState(@PathVariable int id, @PathVariable int state){
        reimburseService.setState(id,state);
    }


}
