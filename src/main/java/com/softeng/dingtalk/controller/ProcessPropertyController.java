package com.softeng.dingtalk.controller;
import com.alibaba.fastjson.JSONObject;
import com.softeng.dingtalk.entity.ProcessProperty;
import com.softeng.dingtalk.service.ProcessPropertyService;
import com.softeng.dingtalk.vo.ProcessPropertyDetailVO;
import com.softeng.dingtalk.vo.ProcessPropertyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProcessPropertyController {
    @Autowired
    ProcessPropertyService processPropertyService;
    @PostMapping("/process")
    public void addProcessProperty(@RequestParam(value = "file", required = false) MultipartFile file,
                                   @RequestParam(value = "processPropertyVOJsonStr") String ProcessPropertyVOJsonStr,
                                    @RequestAttribute int uid){
        ProcessPropertyVO processPropertyVO= JSONObject.parseObject(ProcessPropertyVOJsonStr,ProcessPropertyVO.class);
        if(processPropertyVO.getId()==null){
            processPropertyService.addProcessProperty(file,processPropertyVO,uid);
        }else{
            processPropertyService.updateProcessProperty(processPropertyVO);
        }
    }

    @DeleteMapping("/process/{id}")
    public void deleteProcessProperty(@PathVariable int id){
        processPropertyService.deleteProcessProperty(id);
    }

    @GetMapping("/process/page/{page}/{size}")
    public Map<String, Object> getProcessProperty(@PathVariable int page, @PathVariable int size){
        return processPropertyService.getProcessProperty(page,size);
    }

    @GetMapping("/process/{id}")
    public ProcessPropertyDetailVO getProcessPropertyInfo(@PathVariable int id){
        return processPropertyService.getProcessPropertyDetail(id);
    }

    @PostMapping("/process/{id}")
    public void addProcessFile(@RequestParam MultipartFile file, @RequestParam String fileType,
                               @PathVariable int id){
        processPropertyService.addProcessFile(file,fileType,id);
    }

    @DeleteMapping("/process/{processId}/processFile/{fileId}/processFileType/{type}")
    public void deleteProcessFile(@PathVariable int processId, @PathVariable int fileId, @PathVariable String type){
        processPropertyService.deleteProcessFile(processId,fileId,type);
    }

    @GetMapping("/processFile/{id}")
    public void downloadProcessFile(@PathVariable int id, HttpServletResponse response) {
        processPropertyService.downloadProcessFile(id,response);
    }
}
