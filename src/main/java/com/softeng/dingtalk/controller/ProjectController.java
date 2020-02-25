package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.Project;
import com.softeng.dingtalk.service.ProjectService;
import com.softeng.dingtalk.vo.ProjectVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
