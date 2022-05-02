package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.Reimbursement;
import com.softeng.dingtalk.service.ReimburseService;
import com.softeng.dingtalk.vo.ReimbursementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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

    @GetMapping("/reimburse/page/{page}/{size}")
    public Map<String,Object> getReimbursementList(@PathVariable int page,@PathVariable int size){
        return reimburseService.getReimbursementList(page,size);
    }

    @GetMapping("/reimburse/{id}")
    public Reimbursement getReimbursementDetail(@PathVariable int id){
        return reimburseService.getReimbursementDetail(id);
    }

    @DeleteMapping("/reimbursement/{id}")
    public void deleteReimbursement(@PathVariable int id){
        reimburseService.deleteReimbursement(id);
    }

    @PutMapping("/reimburse/{id}/state/{state}")
    public void setState(@PathVariable int id, @PathVariable int state){
        reimburseService.setState(id,state);
    }

    @PostMapping("/reimburse/{id}/reimbursementFile")
    public void addReimbursementFile(@PathVariable int id, @RequestParam MultipartFile file, @RequestParam String description){
        reimburseService.addReimbursementFile(id,file,description);
    }

    @DeleteMapping("/reimbursementFile/{id}")
    public void deleteReimbursementFile(@PathVariable int id){
        reimburseService.deleteReimbursementFile(id);
    }

}
