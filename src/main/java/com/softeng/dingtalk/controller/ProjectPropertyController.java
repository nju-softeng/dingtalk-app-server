package com.softeng.dingtalk.controller;
import com.softeng.dingtalk.entity.ProjectProperty;
import com.softeng.dingtalk.service.ProjectPropertyService;
import com.softeng.dingtalk.vo.ProjectPropertyFileVO;
import com.softeng.dingtalk.vo.ProjectPropertyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

    @DeleteMapping("/projectProperty/{id}")
    public void deleteProjectProperty(@PathVariable int id){
        projectPropertyService.deleteProjectProperty(id);
    }

    @GetMapping("/projectProperty/page/{page}/{size}")
    public Map<String, Object> getProjectPropertyList(@PathVariable int page, @PathVariable int size){
        return projectPropertyService.getProjectPropertyList(page,size);
    }

    @GetMapping("/projectProperty/{id}")
    public ProjectProperty getProjectPropertyDetail(@PathVariable int id){
        return projectPropertyService.getProjectPropertyDetail(id);
    }

    @PostMapping("/projectProperty/{id}/projectPropertyVersion")
    public void addProjectPropertyVersion(@RequestParam MultipartFile codeFile,@RequestParam MultipartFile reportFile,
                                          @RequestParam String name,@PathVariable int id){
        projectPropertyService.addProjectPropertyVersion(codeFile,reportFile,name,id);
    }

    @PutMapping("/projectPropertyVersion/{id}")
    public void updateProjectPropertyVersion(@PathVariable int id, @RequestBody ProjectPropertyFileVO projectPropertyFileVO){
        projectPropertyService.updateProjectPropertyVersion(id,projectPropertyFileVO);
    }

    @DeleteMapping("/projectPropertyVersion/{id}")
    public void deleteProjectPropertyVersion(@PathVariable int id){
        projectPropertyService.deleteProjectPropertyVersion(id);
    }

    @PostMapping("/projectPropertyFile/{id}/fileType/{type}")
    public String addProjectPropertyFile(@RequestParam MultipartFile file,@PathVariable int id,@PathVariable String type){
        return projectPropertyService.addProjectPropertyFile(file,id,type);
    }

    @DeleteMapping("/projectPropertyFile/{id}/fileType/{type}")
    public void deleteProjectPropertyFile(@PathVariable int id,@PathVariable String type){
        projectPropertyService.deleteProjectPropertyFile(id,type);
    }

    @GetMapping("/projectPropertyFile/{id}/fileType/{type}")
    public void downloadProjectPropertyFile(@PathVariable int id, @PathVariable String type, HttpServletResponse response){
        projectPropertyService.downloadProjectPropertyFile(id,type,response);
    }
}
