package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Project;
import com.softeng.dingtalk.entity.ProjectDetail;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.ProjectDetailRepository;
import com.softeng.dingtalk.repository.ProjectRepository;
import com.softeng.dingtalk.vo.ProjectVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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
    ProjectDetailRepository projectDetailRepository;
    @Autowired
    UserService userService;

    // 添加任务
    public void addProject(ProjectVO projectVO) {
        Project project = new Project(projectVO.getName(), new User(projectVO.getAuditorid()), projectVO.getDates()[0], projectVO.getDates()[1]);
        projectRepository.save(project);

        List<String> userids = projectVO.getDingIds(); // 获取分配者的userid;
        List<ProjectDetail> projectDetails = new ArrayList<>();
        for (String u : userids) {
            int uid = userService.getIdByUserid(u);
            ProjectDetail pd = new ProjectDetail(project, new User(uid));
            projectDetails.add(pd);
        }
        projectDetailRepository.saveAll(projectDetails);
    }


    public List<Project> listProjectByAuditor(int uid) {
        return null;
    }




}
