package com.softeng.dingtalk.controller;

import com.alibaba.fastjson.JSON;
import com.softeng.dingtalk.entity.Patent;
import com.softeng.dingtalk.service.PatentService;
import com.softeng.dingtalk.vo.PatentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api")
public class PatentController {
    @Autowired
    PatentService patentService;
    @PostMapping("/patent")
    public  void addPatent(@RequestParam MultipartFile file,@RequestParam String patentVOJsonStr, @RequestAttribute int id){
        PatentVO patentVO= JSON.parseObject(patentVOJsonStr,PatentVO.class);
        patentVO.setApplicantId(id);
        patentService.addPatent(file,patentVO);
    }
    @GetMapping("/patent/page/{page}/{size}")
    public void getPatentList(@PathVariable int page,@PathVariable int size){
        patentService.getPatentList(page,size);
    }

    @GetMapping("/patent/{id}")
    public Patent getPatentDetail(@PathVariable int id){
        return patentService.getPatentDetail(id);
    }

    @DeleteMapping("/patent/{id}")
    public void deletePatent(@PathVariable int id){

    }


}
