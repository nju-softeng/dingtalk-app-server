package com.softeng.dingtalk.controller;

import com.alibaba.fastjson.JSON;
import com.softeng.dingtalk.entity.Patent;
import com.softeng.dingtalk.service.FileService;
import com.softeng.dingtalk.service.PatentService;
import com.softeng.dingtalk.vo.PatentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class PatentController {
    @Autowired
    PatentService patentService;
    @Autowired
    FileService fileService;
    @PostMapping("/patent")
    public  void addPatent(@RequestParam MultipartFile file,@RequestParam String patentVOJsonStr, @RequestAttribute int uid){
        PatentVO patentVO= JSON.parseObject(patentVOJsonStr,PatentVO.class);
        patentVO.setApplicantId(uid);
        patentService.addPatent(file,patentVO);
    }
    @GetMapping("/patent/page/{page}/{size}")
    public Map<String, Object> getPatentList(@PathVariable int page, @PathVariable int size){
        return patentService.getPatentList(page,size);
    }

    @GetMapping("/patent/{id}")
    public Patent getPatentDetail(@PathVariable int id){
        return patentService.getPatentDetail(id);
    }

    @DeleteMapping("/patent/{id}")
    public void deletePatent(@PathVariable int id){
        patentService.deletePatent(id);
    }

    @PutMapping("/patent/{id}/auditState/{isPass}")
    public void decideAudit(@PathVariable int id,@PathVariable boolean isPass,@RequestAttribute int uid){
        patentService.decideAudit(id,isPass,uid);
    }

    @PutMapping("/patent/{id}/authorizationState/{isPass}")
    public void decideAuthorization(@PathVariable int id,@PathVariable boolean isPass,@RequestAttribute int uid){
        patentService.decideAuthorization(id,isPass,uid);
    }

    @PostMapping("/patent/{id}/patentFile/fileType/{type}")
    public void addPatentFile(@RequestParam MultipartFile file,@PathVariable int id, @PathVariable String type){
        patentService.addPatentFile(file,id,type);
    }

    @DeleteMapping("/patent/{id}/patentFile/fileType/{type}")
    public void deletePatentFile(@PathVariable int id, @PathVariable String type){
        patentService.deletePatentFile(id,type);
    }

    @GetMapping("/patent/{id}/patentFile/fileType/{type}")
    public void downloadPatentFile(@PathVariable int id, @PathVariable String type, HttpServletResponse response){
        patentService.downloadPatentFile(id,type,response);
    }


}
