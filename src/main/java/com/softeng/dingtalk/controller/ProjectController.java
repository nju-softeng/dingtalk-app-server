package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.po.*;
import com.softeng.dingtalk.service.IterationService;
import com.softeng.dingtalk.service.ProjectService;
import com.softeng.dingtalk.vo.IterateAcVO;
import com.softeng.dingtalk.vo.IterationVO;
import com.softeng.dingtalk.vo.ProjectVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @param projectVO, uid
     **/
    @PostMapping("/project")
    public void createOrUpdateProject(@RequestBody ProjectVO projectVO, @RequestAttribute int uid) {
        if (projectVO.getId() == 0) {
            projectService.createProject(projectVO, uid);
        } else {
            projectService.updateProject(projectVO);
        }
    }


    /**
     * 删除项目
     * @param pid
     */
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
     * 审核人查询项目
     * @param uid
     */
    @GetMapping("/project")
    public List<Map<String, Object>> listProjectInfo(@RequestAttribute("uid") int uid) {
        return projectService.listProjectInfo(uid);
    }


    @GetMapping("/project/all")
    public List<Map<String, Object>> listAllProjectInfo() {
        return projectService.listAllProjectInfo();
    }


    /**
     * 查询迭代
     * @param id
     * @return
     */
    @GetMapping("/project/iteration/{id}")
    public IterationPo getIteration(@PathVariable int id) {
        return iterationService.getIterationById(id);
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


    /**
     * 查询项目的所有迭代
     * @param pid
     * @return
     */
    @GetMapping("project/{pid}/iteration")
    public List<IterationPo> listProjectIterations(@PathVariable int pid) {
        return iterationService.listProjectIterations(pid);
    }


    @GetMapping("project/iteration/{itid}/delete")
    public void rmIteration(@PathVariable int itid) {
        iterationService.rmIteration(itid);
    }


    /**
     * 确认项目完成，并自动计算Ac
     * @param itid
     * @param map
     * @param update
     */
    @PostMapping("/project/autosetac/{itid}")
    public void autoSetProjectAc(@PathVariable int itid, @RequestBody Map<String, LocalDate> map, boolean update) {
        iterationService.setIterationAc(itid, map.get("finishdate"));
    }


    /**
     * 确认项目完成，手动输入AC
     * @param itid
     * @param vo
     */
    @PostMapping("/project/manualsetac/{itid}")
    public void manualSetProjectAc(@PathVariable int itid, @Valid @RequestBody IterateAcVO vo) {
        iterationService.manualSetIterationAc(itid, vo.getIterationDetailPos(), vo.getFinishdate());
    }


    /**
     * 计算项目Ac，返回给前端
     * @param itid
     * @param map
     * @param update
     * @return
     */
    @PostMapping("/project/iteration/{itid}/computeac")
    public Map computeIterationAc(@PathVariable int itid, @RequestBody Map<String, LocalDate> map, boolean update) {
        return iterationService.computeIterationAc(itid, map.get("finishdate"));
    }


    @GetMapping("/project/iteration/user")
    public List<IterationPo> listUserIteration(@RequestAttribute int uid) {
        return iterationService.listUserIteration(uid);
    }


}
