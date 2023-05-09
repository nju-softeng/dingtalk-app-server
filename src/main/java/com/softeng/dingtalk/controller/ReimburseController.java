package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.dto.CommonResult;
import com.softeng.dingtalk.dto.req.ReimbursementReq;
import com.softeng.dingtalk.po_entity.Reimbursement;
import com.softeng.dingtalk.service.ReimburseService;
import com.softeng.dingtalk.vo.ReimbursementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ReimburseController {
    @Autowired
    ReimburseService reimburseService;
    @PostMapping("/reimburse")
    public void addReimbursement(@RequestBody ReimbursementVO reimbursementVO,@RequestAttribute int uid){
        if(reimbursementVO.getId()==null){
            reimburseService.addReimbursement(reimbursementVO,uid);
        } else {
            reimburseService.updateReimbursement(reimbursementVO,uid);

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

    @GetMapping("/reimbursementFile/{id}")
    public void downloadReimbursementFile(@PathVariable int id, HttpServletResponse response){
        reimburseService.downloadReimbursementFile(id,response);
    }

    @PostMapping("/v2/reimbursement/{page}/{size}")
    public CommonResult<Map<String, Object>>  queryReimbursementList(@PathVariable int page, @PathVariable int size, @RequestBody ReimbursementReq reimbursementReq) {
        return CommonResult.success(reimburseService.queryReimbursementList(page, size, reimbursementReq));
    }
}
