package com.softeng.dingtalk.controller;

import com.alibaba.fastjson.JSONObject;
import com.softeng.dingtalk.entity.Dissertation;
import com.softeng.dingtalk.service.DissertationPropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class DissertationPropertyController {
    @Autowired
    DissertationPropertyService dissertationPropertyService;

    @PostMapping("/dissertation")
    public void addDissertation(@RequestParam(value = "file") MultipartFile file,
                                @RequestParam(value = "dissertationJsonStr") String dissertationJsonStr,
                                @RequestAttribute int uid){
        Dissertation dissertation= JSONObject.parseObject(dissertationJsonStr, Dissertation.class);
        if(dissertation.getId()==null){
            dissertationPropertyService.addDissertation(file,dissertation);
        }else{
            dissertationPropertyService.updateDissertation(dissertation);
        }
    }

    @GetMapping("/dissertation/page/{page}/{size}")
    public Map<String, Object> getDissertationInfo(@PathVariable int page, @PathVariable int size){
        return dissertationPropertyService.getDissertation(page,size);
    }

    @DeleteMapping("/dissertation/{id}")
    public void deleteDissertation(@PathVariable int id){
        dissertationPropertyService.deleteDissertation(id);
    }

    @PostMapping("/dissertation/{id}/file/{fileType}")
    public void addDissertationFile(@RequestParam(value = "file") MultipartFile file){

    }

    @PostMapping("/dissertation/{id}/file/{fileType}")
    public void deleteDissertationFile(@RequestParam(value = "file") MultipartFile file){

    }

    @PostMapping("/dissertation/")
    public void downloadDissertationFile(@RequestParam(value = "file") MultipartFile file){

    }
}
