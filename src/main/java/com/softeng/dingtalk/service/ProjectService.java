package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.DateUtils;
import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 2/25/2020 2:08 PM
 */
@Service
@Transactional
@Slf4j
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    IterationRepository iterationRepository;
    @Autowired
    IterationDetailRepository iterationDetailRepository;
    @Autowired
    UserService userService;
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    DateUtils dateUtils;
    

    /**
     * 创建项目
     * @param project
     */
    public void createProject(Project project) {
        projectRepository.save(project);
    }


    /**
     * 查询审核人创建的项目
     * @param aid 审核人id
     * @return
     */
    public List<Map<String, Object>> listProjectInfo(int aid) {
        return projectRepository.listProjectInfo(aid);
    }


    /**
     * 查询所有项目信息
     * @return
     */
    public List<Map<String, Object>> listAllProjectInfo() {
        return projectRepository.listAllProjectInfo();
    }


    /**
     * 删除项目
     * @param pid
     */
    public void rmProject(int pid) {
        Project p = projectRepository.findById(pid).get();
        if (p.getCnt() != 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "项目的迭代不为空,无法删除");
        }
        projectRepository.deleteById(pid);
    }


    /**
     * 更新项目
     * @param project
     */
    public void updateProject(Project project) {
        projectRepository.updateTitle(project.getId(), project.getTitle());
    }


}
