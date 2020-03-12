package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.Project;
import com.softeng.dingtalk.entity.ProjectDetail;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.repository.ProjectDetailRepository;
import com.softeng.dingtalk.repository.ProjectRepository;
import com.softeng.dingtalk.vo.ProjectVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
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
    ProjectDetailRepository projectDetailRepository;
    @Autowired
    UserService userService;
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    AcRecordRepository acRecordRepository;

    // 添加任务
    public void addProject(ProjectVO projectVO) {
        Project project = new Project(projectVO.getName(), new User(projectVO.getAuditorid()), projectVO.getDates()[0], projectVO.getDates()[1]);
        int day = Period.between(projectVO.getDates()[0], projectVO.getDates()[1]).getDays();
        project.setExpectedAC(day * projectVO.getDingIds().size() / 30);


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

    // 更新项目信息
    public void updateProject(ProjectVO projectVO) {
        projectRepository.updateProject(projectVO.getId(), projectVO.getName(), projectVO.getDates()[0], projectVO.getDates()[1]);
        if (projectVO.isUpdateDingIds()) {
            projectDetailRepository.deleteByProjectId(projectVO.getId());  // 删除旧的分配信息
            List<String> userids = projectVO.getDingIds(); // 获取分配者的userid;
            List<ProjectDetail> projectDetails = new ArrayList<>();
            Project project = new Project(projectVO.getId());
            for (String u : userids) {
                int uid = userService.getIdByUserid(u);
                ProjectDetail pd = new ProjectDetail(project, new User(uid));
                projectDetails.add(pd);
            }
            projectDetailRepository.saveAll(projectDetails);
        }

    }

    // 统计时间段周日的次数
    private int countSunday(LocalDate btime, LocalDate ftime) {
        int day = Period.between(btime, ftime).getDays();
        day += (btime.getDayOfWeek().getValue()-1); // 前补

        if (ftime.getDayOfWeek().getValue() == 7) { // 后砍
            return (day - ftime.getDayOfWeek().getValue()) / 7 + 1;
        } else {
            return (day - ftime.getDayOfWeek().getValue()) / 7;
        }
    }

    /**
     * 计算ac值
     * 实际ac计算公式: 𝐴_𝑖=𝐴_𝑎∗𝐷_𝑖/(∑𝐷)∗𝐷_𝑖/0.5
     * 𝐴_𝑖 denotes individual actual reward
     * 𝐴_𝑎 denotes team acutal reward
     * 𝐷_𝑖  denotes individual average DC during the iteration
     **/
    public void computeProjectAc(Project project) {
        List<ProjectDetail> projectDetails = projectDetailRepository.findAllByProject(project);
        int day = Period.between(project.getBeginTime(), project.getFinishTime()).getDays();
        double actualAc = day * projectDetails.size() / 30; // 总ac值 = 实际时间 * 参与人数 / 30
        double totalDc = 0;
        double[] dcList = new double[projectDetails.size()]; // 记录各参与者开发周期内的dc值
        int index = 0;
        for (ProjectDetail pd : projectDetails) {
            double dc = dcRecordRepository.getByTime(pd.getUser().getId(), project.getAuditor().getId(), project.getBeginTime(), project.getFinishTime());
            dcList[index++] = dc;
            log.debug(dc + "");
            totalDc += dc;
        }
        log.debug("totaldc:" + totalDc );

        if (totalDc == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "项目参与者的总dc值为0，可能是参与者未提交dc申请，无法计算，需人工决定");
        }

        index = 0;
        int week = countSunday(project.getBeginTime(), project.getFinishTime());
        for (ProjectDetail pd : projectDetails) {
<<<<<<< Updated upstream
            double ac = actualAc * dcList[index] / totalDc * dcList[index] / week * 2; // 计算实际AC
=======
            // 计算实际AC
            if (pd.getAcRecord() != null) {
                acRecordRepository.delete(pd.getAcRecord());
            }
            double ac = actualAc * dcList[index] / totalDc * dcList[index] / week * 2;
            ac = (double) (Math.round(ac * 1000)/1000.0);
>>>>>>> Stashed changes
            index++;
            log.debug("个人实际ac: " + ac);
            AcRecord acRecord = new AcRecord(pd.getUser(), project.getAuditor(), ac, project.getName());
            acRecordRepository.save(acRecord); // 实例化ac记录
            pd.setAcRecord(acRecord);
            projectDetailRepository.save(pd);
<<<<<<< Updated upstream
        }
=======
            acRecords.add(acRecord);
        }
        return acRecords;
    }

    // 自定义项目的ac值
    public List<AcRecord> manualSetProjectAc(int pid, List<ProjectDetail> projectDetails) {
        Project project = projectRepository.findById(pid).get();
        project.setStatus(true);
        // 作为返回值，交给切面
        List<AcRecord> acRecords = new ArrayList<>();
        for (ProjectDetail pd : projectDetails) {
            ProjectDetail projectDetail = projectDetailRepository.findById(pd.getId()).get();
            // 删除之前的 acrecord
            if (projectDetail.getAcRecord() != null) {
                acRecordRepository.delete(projectDetail.getAcRecord());
            }
            projectDetail.setAc(pd.getAc());
            AcRecord acRecord = new AcRecord(pd.getUser(), project.getAuditor(), pd.getAc(), "完成开发任务: " + project.getName(), AcRecord.PROJECT);
            acRecordRepository.save(acRecord);
            projectDetail.setAcRecord(acRecord);
            acRecords.add(acRecord);
        }
        return acRecords;
}


    // 计算AC返回给前端
    public Map ComputeProjectAc(int pid, LocalDate finishTime) {
        Project p =  projectRepository.findById(pid).get();
        List<ProjectDetail> projectDetails = projectDetailRepository.findAllByProject(p);
        int day = (int) p.getBeginTime().until(finishTime, ChronoUnit.DAYS);
        double actualAc = day * projectDetails.size() / 30.0; // 总ac值 = 实际时间 * 参与人数 / 30
        double totalDc = 0;
        double[] dcList = new double[projectDetails.size()]; // 记录各参与者开发周期内的dc值
        int index = 0;
        for (ProjectDetail pd : projectDetails) {
            double dc = dcRecordRepository.getByTime(pd.getUser().getId(), p.getAuditor().getId(), p.getBeginTime(), finishTime);
            dcList[index++] = dc;
            totalDc += dc;
        }

        if (totalDc == 0) {
            return Map.of("valid", false);
        }

        index = 0;
        // 迭代周期所跨周数
        int week = countWeek(p.getBeginTime(), finishTime);

        List<Map<String, Object>> res = new ArrayList<>();

        for (ProjectDetail pd : projectDetails) {
            // 计算实际AC
            double ac = actualAc * dcList[index] / totalDc * dcList[index] / week * 2;
            ac = (double) (Math.round(ac * 1000)/1000.0);

            res.add(Map.of("name", pd.getUser().getName(), "ac", ac, "dc", dcList[index]));
            index++;
        }

        totalDc = (double) (Math.round(totalDc * 1000)/1000.0);

        return Map.of("valid", true, "res", res, "actualAc", actualAc, "week", week, "totalDc", totalDc);
>>>>>>> Stashed changes
    }

    public Object getProjectDc(int pid) {
        Project p =  projectRepository.findById(pid).get();
        List<Map<String, String>> dclist = projectDetailRepository.getProjectDc(pid, p.getAuditor().getId(), p.getBeginTime(), p.getEndTime());
        Map<String, List<Map<String, String>>> maplist = dclist.stream()
                .collect(Collectors.groupingBy(map -> map.get("name"),
                        Collectors.mapping(map -> {
                            Map<String, String> temp = new HashMap<String, String>(map);
                            temp.remove("name");
                            return temp;
                        }, Collectors.toList())));

        List<Map<String, Object>> res = new ArrayList<>();
        for (String key : maplist.keySet()) {
            res.add(Map.of("name", key, "dclist", maplist.get(key)));
        }
        return res;
    }


    // 审核人查询进行中的项目
    public List<Project> listUnfinishProjectByAuditor(int aid) {
        return projectRepository.listUnfinishProjectByAid(aid);
    }


    // 审核人已经结束的进行中的项目
    public List<Project> listfinishProjectByAuditor(int aid) {
        return projectRepository.listfinishProjectByAid(aid);
    }


    // 用户获取正在进行的项目
    public List<Project>  listProjectByUid(int uid) {
        return projectDetailRepository.listProjectByUid(uid);
    }


    // 删除项目
    public void delete(int id) {
        projectRepository.deleteById(id);
    }



}
