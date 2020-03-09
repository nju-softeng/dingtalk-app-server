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

    // 添加 或 更新 项目
    @PostMapping("/project")
    public void addUpdateProject(@RequestBody ProjectVO projectVO) {
        if (projectVO.getId() == 0) {
            projectService.addProject(projectVO);
        } else {
            projectService.updateProject(projectVO);
        }

    }

    @GetMapping("/project/dc/{pid}")
    public Object getProjectDc(@PathVariable int pid) {
        return projectService.getProjectDc(pid);
    }


    // 查询进行中的项目
    @GetMapping("/project/unfinish/{aid}")
    public List<Project> listUnfinishProjectByAuditor(@PathVariable int aid) {
        return projectService.listUnfinishProjectByAuditor(aid);
    }


    // 查询已经结束的项目
    @GetMapping("/project/finish/{aid}")
    public List<Project> listfinishProjectByAuditor(@PathVariable int aid) {
        return projectService.listfinishProjectByAuditor(aid);
    }


    // 删除项目
    @GetMapping("/project/delete/{id}")
    public void deleteProject(@PathVariable int id) {
        projectService.delete(id);
    }

}
