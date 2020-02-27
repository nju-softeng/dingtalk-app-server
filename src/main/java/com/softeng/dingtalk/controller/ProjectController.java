package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.Project;
import com.softeng.dingtalk.service.ProjectService;
import com.softeng.dingtalk.vo.ProjectVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 2/25/2020 2:09 PM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @PostMapping("/project")
    public void addProject(@RequestBody ProjectVO projectVO) {
        projectService.addProject(projectVO);
    }

    @GetMapping("/project/unfinish/{aid}")
    public List<Project> listUnfinishProjectByAuditor(@PathVariable int aid) {
        return projectService.listUnfinishProjectByAuditor(aid);
    }


}
