package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.service.IterationService;
import com.softeng.dingtalk.service.ProjectService;
import com.softeng.dingtalk.vo.IterateAcVO;
import com.softeng.dingtalk.vo.IterationVO;
import com.softeng.dingtalk.vo.ProjectVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    @Autowired
    IterationService iterationService;




    /**
     * 审核人创建或更新项目
     * @param project, uid
     **/
    @PostMapping("/project")
    public void createOrUpdateProject(@RequestBody Project project, @RequestAttribute int uid) {
        if (project.getId() == 0) {
            project.setAuditor(new User(uid));
            projectService.createProject(project);
        } else {
            projectService.updateProject(project);
        }
    }


    @GetMapping("/project/{pid}/delete")
    public void rmProject(@PathVariable int pid) {
        projectService.rmProject(pid);
    }



    /**
     * 创建或修改迭代
     * @param pid, iterationVO
     **/
    @PostMapping("/project/{pid}/iteration")
    public void createIteration(@PathVariable int pid, @RequestBody IterationVO iterationVO) {
        if(iterationVO.getId() == 0) {
            // 创建迭代
            iterationService.createIteration(pid, iterationVO);
        } else {
            // 更新迭代
            iterationService.updateIteration(iterationVO);
        }
    }


    /**
     * 查询项目
     * @param uid
     */
    @GetMapping("/project")
    public List<Map<String, Object>> listProjectInfo(@RequestAttribute("uid") int uid) {
        return iterationService.listProjectInfo(uid);
    }


    /**
     * 获取项目及迭代信息
     * @param pid
     * @return
     */
    @GetMapping("project/{pid}/detail")
    public Map getProjectDetail(@PathVariable int pid) {
        return iterationService.listProjectDetail(pid);
    }


    @GetMapping("project/iteration/{itid}/delete")
    public void rmIteration(@PathVariable int itid) {
        iterationService.rmIteration(itid);
    }


    // 确认项目完成，并自动计算Ac
    @PostMapping("/project/autosetac/{itid}")
    public void autoSetProjectAc(@PathVariable int itid, @RequestBody Map<String, LocalDate> map, boolean update) {
        iterationService.setIterationAc(itid, map.get("finishdate"));
    }



    // 确认项目完成，手动输入AC
    @PostMapping("/project/manualsetac/{itid}")
    public void manualSetProjectAc(@PathVariable int itid, @Valid @RequestBody IterateAcVO vo) {
        iterationService.manualSetIterationAc(itid, vo.getIterationDetails(), vo.getFinishdate());
    }


    /**
     * 计算项目Ac，返回给前端
     * @param itid
     * @param map
     * @return
     */
    @PostMapping("/project/iteration/{itid}/computeac")
    public Map computeIterationAc(@PathVariable int itid, @RequestBody Map<String, LocalDate> map, boolean update) {
        return iterationService.computeIterationAc(itid, map.get("finishdate"));
    }




//
//    @PostMapping("/project/dc/{pid}")
//    public Object getProjectDc(@PathVariable int pid, @RequestBody Map<String, LocalDate> map) {
//        return projectService.getProjectDc(pid, map.get("finishdate"));
//    }
//
//    @PostMapping("/project/computeac/{pid}")
//    public Map ComputeProjectAc(@PathVariable int pid, @RequestBody Map<String, LocalDate> map) {
//        return projectService.ComputeProjectAc(pid, map.get("finishdate"));
//    }
//

//
//    // 确认项目完成，手动设置Ac
//    @PostMapping("/project/manualsetac/{pid}")
//    public void manualSetProjectAc(@PathVariable int pid,@Valid @RequestBody List<ProjectDetail> projectDetails) {
//        projectService.manualSetProjectAc(pid, projectDetails);
//    }
//
//    // 查询进行中的项目
//    @GetMapping("/project/unfinish/{aid}")
//    public List<Project> listUnfinishProjectByAuditor(@PathVariable int aid) {
//        return projectService.listUnfinishProjectByAuditor(aid);
//    }
//
//
//    // 查询已经结束的项目
//    @GetMapping("/project/finish/{aid}")
//    public List<Project> listfinishProjectByAuditor(@PathVariable int aid) {
//        return projectService.listfinishProjectByAuditor(aid);
//    }
//
//
//    // 删除项目
//    @GetMapping("/project/delete/{id}")
//    public void deleteProject(@PathVariable int id) {
//        projectService.delete(id);
//    }
//
//
//    // 开发者查询自己的项目
//    @GetMapping("/project/dev")
//    public List<Project> listDevProject(@RequestAttribute int uid) {
//        return projectService.listDevProject(uid);
//    }

}
