package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.vo.DcVO;
import com.softeng.dingtalk.vo.IterateInfoVO;
import com.softeng.dingtalk.vo.IterationVO;
import com.softeng.dingtalk.vo.ProjectVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    Utils utils;
    

    /**
     * 创建项目
     * @param project
     */
    public void createProject(Project project) {
        projectRepository.save(project);
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
