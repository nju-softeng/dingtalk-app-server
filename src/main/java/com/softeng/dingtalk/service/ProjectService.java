package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.vo.DcVO;
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
    public void rmProjecj(int pid) {
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


    /**
     * 创建迭代
     * @param pid
     * @param vo
     */
    public void createIteration(int pid, IterationVO vo) {
        Project p = projectRepository.findById(pid).get();
        LocalDate[] dates = vo.getDates();
        Iteration iteration = new Iteration(p.getCnt() + 1, p.getAuditor(), dates[0], dates[1]);
        int day = (int) dates[0].until(dates[1], ChronoUnit.DAYS);
        iteration.setExpectedAC(day * vo.getDingIds().size() / 30.0);
        iteration.setProject(p);
        iterationRepository.save(iteration);
        // 更新项目的迭代
        p.setCurIteration(iteration.getId());
        // 获取分配者的userid;
        p.setCnt(p.getCnt()+1);
        List<String> userids = vo.getDingIds();
        List<IterationDetail> iterationDetails = new ArrayList<>();
        for (String u : userids) {
            // 根据userid 查询 uid
            int uid = userService.getIdByUserid(u);
            IterationDetail itd = new IterationDetail(iteration, new User(uid));
            iterationDetails.add(itd);
        }
        iterationDetailRepository.saveAll(iterationDetails);
    }


    /**
     * 更新迭代
     * @param vo 前端接收的数据
     **/
    public void updateIteration(IterationVO vo) {
        Iteration it = iterationRepository.findById(vo.getId()).get();
        LocalDate[] dates = vo.getDates();
        it.setBeginTime(vo.getDates()[0]);
        it.setEndTime(vo.getDates()[1]);
        int day = (int) dates[0].until(dates[1], ChronoUnit.DAYS);
        it.setExpectedAC(day * vo.getDingIds().size() / 30.0);
        if (vo.isUpdateDingIds()) {
            // 删除旧的 iterationDetail
            iterationDetailRepository.deleteByIterationId(vo.getId());
            List<IterationDetail> iterationDetails = new ArrayList<>();
            for (String u : vo.getDingIds()) {
                int uid = userService.getIdByUserid(u);
                IterationDetail itd = new IterationDetail(it, new User(uid));
                iterationDetails.add(itd);
            }
            iterationDetailRepository.saveAll(iterationDetails);
        }

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
     * 查询项目详细信息
     * @param pid
     * @return
     */
    public Map listProjectDetail(int pid) {
        List<Iteration> iterations = iterationRepository.listIterationByPid(pid);
        Project p = projectRepository.findById(pid).get();
        return Map.of("iterations", iterations, "title", p.getTitle(),"icnt", p.getCnt(), "success", p.getSuccessCnt());
    }

    /**
     * 删除迭代
     * @param itid
     */
    public void rmIteration(int itid) {
        Iteration it = iterationRepository.findById(itid).get();
        Project p = it.getProject();
        // 修改项目迭代次数
        p.setCnt(p.getCnt() - 1);
        // 修改项目当前迭代版本
        p.setCurIteration(it.getPrevIteration());
        projectRepository.save(p);
        iterationRepository.deleteById(itid);
    }

    //int day = (int) btime.until(ftime,ChronoUnit.DAYS) + 1;
    private static int fib(int n) {
        if(n==1 || n==2){
            return 1;
        }
        int sec=1;//第二项
        int fir=1;//第一项
        int temp=0;//临时项
        for (int i = 3; i <= n; i++) {
            temp=sec;
            sec=fir+sec;
            fir=temp;
        }
        return sec;
    }


    //

    /**
     * 计算迭代AC值
     * @param itid
     * @param finishdate
     */
    public void setIterationAc(int itid, LocalDate finishdate) {
        Iteration iteration = iterationRepository.findById(itid).get();
        Project project = iteration.getProject();
        int predictDay = (int) iteration.getBeginTime().until(iteration.getEndTime(), ChronoUnit.DAYS) + 1;
        int actualDay = (int) iteration.getBeginTime().until(finishdate, ChronoUnit.DAYS) + 1;

        // AC扣除
        double AcReduce = 0;
        if (predictDay >= actualDay) {
            log.debug("按时交付");
            // 连续按时交付次数加 1
            project.setSuccessCnt(project.getSuccessCnt() + 1);
        } else {
            log.debug("延期交付");
            // 连续奖励中断
            project.setSuccessCnt(0);
            int delay = (int) iteration.getEndTime().until(iteration.getFinishTime(), ChronoUnit.DAYS);
            if (Math.ceil(delay / 7.0) < 1.0) {
                AcReduce = Math.ceil(delay / 7.0) * 0.15;
            } else {
                AcReduce = Math.ceil(delay / 7.0) * 0.25;
            }
        }
        //迭代人数
        int num = iteration.getIterationDetails().size();
        // 预期AC
        double AcPredict = predictDay * num / 30.0;
        // 实际AC
        double AcActual = actualDay * num /30.0;
        // AC奖励
        double AcAward = fib(project.getSuccessCnt());


        double[] dclist = new double[num];

        int begin = utils.getTimeCode(iteration.getBeginTime());
        int end = utils.getTimeCode(finishdate);
        int index = 0;
        double dcSum = 0;
        for (IterationDetail itd : iteration.getIterationDetails()) {
            double dcAll = dcRecordRepository.getUserDcSumByDate(itd.getUser().getId(), project.getAuditor().getId(), begin, end);
            double dcBeginWeek = dcRecordRepository.getUserDcByWeek(itd.getUser().getId(), project.getAuditor().getId(), begin);
            double dcEndWeek = dcRecordRepository.getUserDcByWeek(itd.getUser().getId(), project.getAuditor().getId(), end);
            double deduct = (dcBeginWeek * (iteration.getBeginTime().getDayOfWeek().getValue() - 1) + dcEndWeek * (7 - finishdate.getDayOfWeek().getValue())) / 7.0;
            // 开发期间周dc均值
            dclist[index] = (dcAll - deduct) * 7.0 / actualDay;
            dcSum += dclist[index];
            index ++;
        }

        if (dcSum == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "项目参与者的总dc值为0，可能是参与者未提交dc申请，无法计算，需自定义");
        }

        double dcSumAvg = dcSum / (double) num;
        index = 0;
        List<AcRecord> acRecords = new ArrayList<>();
        for (IterationDetail itd : iteration.getIterationDetails()) {
            if (itd.getAcRecord() != null) {
                acRecordRepository.delete(itd.getAcRecord());
            }
            // AC 计算公式
            double ac = (AcActual + AcAward - AcReduce) * dclist[index] /dcSumAvg * dclist[index] * 2;
            AcRecord acRecord = new AcRecord(itd.getUser(), project.getAuditor(), ac, "完成开发任务: " + project.getTitle() + " 第" + project.getCnt() + "迭代" , AcRecord.PROJECT);
            // 实例化ac记录
            acRecordRepository.save(acRecord);
            itd.setAcRecord(acRecord);
            itd.setAc(ac);
            iterationDetailRepository.save(itd);
            acRecords.add(acRecord);
            index ++;
        }

        //更新论文和迭代状态
        iteration.setFinishTime(finishdate);
        iteration.setStatus(true);

    }


//    // 更新项目信息
//    public void updateProject(ProjectVO projectVO) {
//        Project p = projectRepository.findById(projectVO.getId()).get();
//        LocalDate[] dates = projectVO.getDates();
//        p.setName(projectVO.getName());
//        p.setBeginTime(dates[0]);
//        p.setEndTime(dates[1]);
//        int day = (int) dates[0].until(dates[1], ChronoUnit.DAYS);
//        p.setExpectedAC(day * projectVO.getDingIds().size() / 30.0);
//
//        if (projectVO.isUpdateDingIds()) {
//            projectDetailRepository.deleteByProjectId(projectVO.getId());  // 删除旧的分配信息
//            List<String> userids = projectVO.getDingIds(); // 获取分配者的userid;
//            List<ProjectDetail> projectDetails = new ArrayList<>();
//            Project project = new Project(projectVO.getId());
//            for (String u : userids) {
//                int uid = userService.getIdByUserid(u);
//                ProjectDetail pd = new ProjectDetail(project, new User(uid));
//                projectDetails.add(pd);
//            }
//            projectDetailRepository.saveAll(projectDetails);
//        }
//
//    }
//
//    // 统计时间段周日的次数
//    public int countWeek(LocalDate btime, LocalDate ftime) {
//        // 时间段天数
//        int day = (int) btime.until(ftime,ChronoUnit.DAYS) + 1;
//        // 开始日期的星期
//        int bweek = btime.getDayOfWeek().getValue();
//        // 结束日期的星期
//        int fweek = ftime.getDayOfWeek().getValue();
//        // 前补: (bweek + 7 - 5) % 7
//        day += (bweek + 2) % 7;
//        // 后砍
//        if (fweek == 4) {
//            return (day - (fweek + 2) % 7 - 1) / 7 + 1;
//        } else {
//            return (day - (fweek + 2) % 7 - 1) / 7;
//        }
//    }
//
//    /**
//     * 计算ac值
//     * 实际ac计算公式: 𝐴_𝑖=𝐴_𝑎∗𝐷_𝑖/(∑𝐷)∗𝐷_𝑖/0.5
//     * 𝐴_𝑖 denotes individual actual reward
//     * 𝐴_𝑎 denotes team acutal reward
//     * 𝐷_𝑖  denotes individual average DC during the iteration
//     **/
//    public List<AcRecord> autoSetProjectAc(int pid, LocalDate finishdate) {
//        Project project = projectRepository.findById(pid).get();
//        project.setStatus(true);
//        project.setFinishTime(finishdate);
//        List<ProjectDetail> projectDetails = projectDetailRepository.findAllByProject(project);
//        int day = (int) project.getBeginTime().until(project.getFinishTime(), ChronoUnit.DAYS);
//        double actualAc = day * projectDetails.size() / 30.0; // 总ac值 = 实际时间 * 参与人数 / 30
//        double totalDc = 0;
//        double[] dcList = new double[projectDetails.size()]; // 记录各参与者开发周期内的dc值
//        int index = 0;
//        for (ProjectDetail pd : projectDetails) {
//            double dc = dcRecordRepository.getByTime(pd.getUser().getId(), project.getAuditor().getId(), project.getBeginTime(), finishdate);
//            dcList[index++] = dc;
//            totalDc += dc;
//        }
//        log.debug("totaldc:" + totalDc );
//
//        if (totalDc == 0) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "项目参与者的总dc值为0，可能是参与者未提交dc申请，无法计算，需自定义");
//        }
//
//        // 作为返回值，交给切面
//        List<AcRecord> acRecords = new ArrayList<>();
//        index = 0;
//        int week = countWeek(project.getBeginTime(),finishdate);
//        for (ProjectDetail pd : projectDetails) {
//
//            // 计算实际AC
//            if (pd.getAcRecord() != null) {
//                acRecordRepository.delete(pd.getAcRecord());
//            }
//            double ac = actualAc * dcList[index] / totalDc * dcList[index] / week * 2;
//
//            ac = (double) (Math.round(ac * 1000)/1000.0);
//
//            index++;
//            log.debug("个人实际ac: " + ac);
//            AcRecord acRecord = new AcRecord(pd.getUser(), project.getAuditor(), ac, "完成开发任务: " + project.getName(), AcRecord.PROJECT);
//            // 实例化ac记录
//            acRecordRepository.save(acRecord);
//            pd.setAcRecord(acRecord);
//            pd.setAc(ac);
//            projectDetailRepository.save(pd);
//
//            acRecords.add(acRecord);
//        }
//        return acRecords;
//    }
//
//    // 自定义项目的ac值
//    public List<AcRecord> manualSetProjectAc(int pid, List<ProjectDetail> projectDetails) {
//        Project project = projectRepository.findById(pid).get();
//        project.setStatus(true);
//        // 作为返回值，交给切面
//        List<AcRecord> acRecords = new ArrayList<>();
//        for (ProjectDetail pd : projectDetails) {
//            ProjectDetail projectDetail = projectDetailRepository.findById(pd.getId()).get();
//            // 删除之前的 acrecord
//            if (projectDetail.getAcRecord() != null) {
//                acRecordRepository.delete(projectDetail.getAcRecord());
//            }
//            projectDetail.setAc(pd.getAc());
//            AcRecord acRecord = new AcRecord(pd.getUser(), project.getAuditor(), pd.getAc(), "完成开发任务: " + project.getName(), AcRecord.PROJECT);
//            acRecordRepository.save(acRecord);
//            projectDetail.setAcRecord(acRecord);
//            acRecords.add(acRecord);
//        }
//        return acRecords;
//
//    }
//
//
//
//    // 计算AC返回给前端
//    public Map ComputeProjectAc(int pid, LocalDate finishTime) {
//        Project p =  projectRepository.findById(pid).get();
//        List<ProjectDetail> projectDetails = projectDetailRepository.findAllByProject(p);
//        int day = (int) p.getBeginTime().until(finishTime, ChronoUnit.DAYS);
//        double actualAc = day * projectDetails.size() / 30.0; // 总ac值 = 实际时间 * 参与人数 / 30
//        double totalDc = 0;
//        double[] dcList = new double[projectDetails.size()]; // 记录各参与者开发周期内的dc值
//        int index = 0;
//        for (ProjectDetail pd : projectDetails) {
//            double dc = dcRecordRepository.getByTime(pd.getUser().getId(), p.getAuditor().getId(), p.getBeginTime(), finishTime);
//            dcList[index++] = dc;
//            totalDc += dc;
//        }
//
//        if (totalDc == 0) {
//            return Map.of("valid", false);
//        }
//
//        index = 0;
//        // 迭代周期所跨周数
//        int week = countWeek(p.getBeginTime(), finishTime);
//
//        List<Map<String, Object>> res = new ArrayList<>();
//
//        for (ProjectDetail pd : projectDetails) {
//            // 计算实际AC
//            double ac = actualAc * dcList[index] / totalDc * dcList[index] / week * 2;
//            ac = (double) (Math.round(ac * 1000)/1000.0);
//
//            res.add(Map.of("name", pd.getUser().getName(), "ac", ac, "dc", dcList[index]));
//            index++;
//        }
//
//        totalDc = (double) (Math.round(totalDc * 1000)/1000.0);
//        return Map.of("valid", true, "res", res, "actualAc", actualAc, "week", week, "totalDc", totalDc);
//    }
//
//
//    // 查询项目期间的dc值
//    public Object getProjectDc(int pid, LocalDate finishTime) {
//        Project p =  projectRepository.findById(pid).get();
//        List<Map<String, String>> dclist = projectDetailRepository.getProjectDc(pid, p.getAuditor().getId(), p.getBeginTime(), finishTime);
//        Map<String, List<Map<String, String>>> maplist = dclist.stream()
//                .collect(Collectors.groupingBy(map -> map.get("name"),
//                        Collectors.mapping(map -> {
//                            Map<String, String> temp = new HashMap<String, String>(map);
//                            temp.remove("name");
//                            return temp;
//                        }, Collectors.toList())));
//
//        List<Map<String, Object>> res = new ArrayList<>();
//
//        List<User> users = projectDetailRepository.findUserByProjectId(pid);
//
//        int week = countWeek(p.getBeginTime(), finishTime);
//
//        for (User u : users) {
//            double dctotal = dcRecordRepository.getByTime(u.getId(), p.getAuditor().getId(), p.getBeginTime(), finishTime);
//            if (maplist.containsKey(u.getName())) {
//                res.add(Map.of("name", u.getName(), "values", maplist.get(u.getName()), "dctotal", dctotal));
//            } else {
//                res.add(Map.of("name", u.getName(), "values", new ArrayList(), "dctotal", dctotal));
//            }
//        }
//
//        return res;
//    }
//
//
//    // 审核人查询进行中的项目
//    public List<Project> listUnfinishProjectByAuditor(int aid) {
//        return projectRepository.listUnfinishProjectByAid(aid);
//    }
//
//
//    // 审核人已经结束的进行中的项目
//    public List<Project> listfinishProjectByAuditor(int aid) {
//        return projectRepository.listfinishProjectByAid(aid);
//    }
//
//
//    // 删除项目
//    public void delete(int id) {
//        projectRepository.deleteById(id);
//    }
//
//    // 开发者人获取自己参与的任务
//    public List<Project> listDevProject(int uid) {
//        List<Integer> pids = projectDetailRepository.listProjectIdByUid(uid);
//        if (pids.size() == 0) {
//            return null;
//        }
//        return projectRepository.findAllById(pids);
//    }



}
