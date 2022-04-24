package com.softeng.dingtalk.controller;
import com.softeng.dingtalk.entity.ProjectProperty;
import com.softeng.dingtalk.service.ProjectPropertyService;
import com.softeng.dingtalk.vo.ProjectPropertyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProjectPropertyController {
    @Autowired
    ProjectPropertyService projectPropertyService;

    @PostMapping("/projectProperty")
    public void addProjectProperty(@RequestBody ProjectPropertyVO projectPropertyVO){
        if(projectPropertyVO.getId()==null){
            projectPropertyService.addProjectProperty(projectPropertyVO);
        }else{
            projectPropertyService.updateProjectProperty(projectPropertyVO);
        }
    }
    @GetMapping("/projectProperty/page/{page}/{size}")
    public Map<String, Object> getProjectPropertyList(@PathVariable int page, @PathVariable int size){
        return projectPropertyService.getProjectPropertyList(page,size);
    }
    @PostMapping("/projectProperty/{id}/projectPropertyVersion")
    public void addProjectPropertyVersion(@RequestParam MultipartFile codeFile,@RequestParam MultipartFile reportFile,
                                          @RequestParam String name,@PathVariable int id){

    }
}
