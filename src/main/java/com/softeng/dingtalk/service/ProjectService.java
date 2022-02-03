package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.DateUtils;
import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.vo.ProjectVO;
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
     *
     * @param projectVO
     */
    public void createProject(ProjectVO projectVO, int uid) {
        Project project = new Project();
        project.setAuditor(new User(uid));
        setSameAttribute(projectVO, project);
        projectRepository.save(project);
    }

    private void setSameAttribute(ProjectVO projectVO, Project project) {
        project.setTitle(projectVO.getName());
        project.setLeader(new User(projectVO.getLeaderId()));
        project.setNature(projectVO.isNature());
        if (projectVO.getHorizontalLevel()>='A' && projectVO.getHorizontalLevel()<='D') {
            project.setHorizontalLevel(projectVO.getHorizontalLevel());
        }
        project.setLongitudinalLevel(projectVO.getLongitudinalLevel());
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
     * @param projectVO
     */
    public void updateProject(ProjectVO projectVO) {
        Project project = new Project();
        setSameAttribute(projectVO, project);
        projectRepository.save(project);
    }


}
