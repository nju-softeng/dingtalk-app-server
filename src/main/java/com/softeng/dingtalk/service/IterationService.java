package com.softeng.dingtalk.service;

import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.vo.IterateInfoVO;
import com.softeng.dingtalk.vo.IterationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 3/19/2020 2:12 PM
 */
@Service
@Transactional
@Slf4j
public class IterationService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    IterationRepository iterationRepository;
    @Autowired
    IterationDetailRepository iterationDetailRepository;
    @Autowired
    SystemService systemService;
    @Autowired
    UserService userService;
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    Utils utils;
    @Autowired
    NotifyService notifyService;
    @Autowired
    PerformanceService performanceService;


    /**
     * 根据id查询迭代
     * @param id
     * @return
     */
    public Iteration getIterationById(int id) {
        return iterationRepository.getIterationById(id);
    }


    /**
     * 查询开发者参与的迭代
     * @return
     */
    public List<Iteration> listUserIteration(int uid) {
         List<Integer> ids = iterationDetailRepository.listIterationIdByUid(uid);
         if (ids.size() != 0) {
             return iterationRepository.findAllById(ids);
         } else {
             return null;
         }
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
        int day = (int) dates[0].until(dates[1], ChronoUnit.DAYS) + 1;
        iteration.setExpectedAC(day * vo.getDingIds().size() / 30.0);
        iteration.setProject(p);
        iteration.setPrevIteration(p.getCurIteration());
        iterationRepository.save(iteration);
        // 更新项目的迭代
        p.setCurIteration(iteration.getId());
        // 获取分配者的userid;
        p.setCnt(p.getCnt()+1);
        List<String> userids = vo.getDingIds();
        List<IterationDetail> iterationDetails = new ArrayList<>();
        for (String u : userids) {
            // 根据userid 查询 uid
            int uid = systemService.getIdByUserid(u);
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
        it.setStatus(false);
        int day = (int) dates[0].until(dates[1], ChronoUnit.DAYS) + 1;
        it.setExpectedAC(day * vo.getDingIds().size() / 30.0);
        if (vo.isUpdateDingIds()) {
            // 删除旧的 iterationDetail
            iterationDetailRepository.deleteByIterationId(vo.getId());
            List<IterationDetail> iterationDetails = new ArrayList<>();
            for (String u : vo.getDingIds()) {
                int uid = systemService.getIdByUserid(u);
                IterationDetail itd = new IterationDetail(it, new User(uid));
                iterationDetails.add(itd);
            }
            iterationDetailRepository.saveAll(iterationDetails);
        }

    }


    /**
     * 查询项目详细信息
     * @param pid
     * @return
     */
    public Map listProjectDetail(int pid) {
        List<Iteration> iterations = iterationRepository.listIterationByPid(pid);
        Project p = projectRepository.findById(pid).get();
        return Map.of("iterations", iterations, "authorid", p.getAuditor().getId()  , "title", p.getTitle(),"icnt", p.getCnt(), "success", p.getSuccessCnt());
    }


    /**
     * 查询项目迭代
     * @param pid
     * @return
     */
    public List<Iteration> listProjectIterations(int pid) {
        return iterationRepository.listIterationByPid(pid);
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
        Integer successCnt = iterationRepository.getConSucessCntById(it.getPrevIteration());
        if (successCnt == null) {
            successCnt = 0;
        }
        p.setSuccessCnt(successCnt);
        projectRepository.save(p);
        iterationRepository.deleteById(itid);
    }



    /**
     * 斐波那契数列
     * @param n
     * @return
     */
    private static int fib(int n) {
        if (n == 0) {
            return 0;
        }

        if(n==1 || n==2){
            return 1;
        }

        int sec = 1, fir = 1, temp = 0;

        for (int i = 3; i <= n; i++) {
            temp = sec;
            sec = fir + sec;
            fir = temp;
        }
        return sec;
    }


    /**
     * 计算迭代的AC返回给前端
     * @param itid
     * @param finishdate
     * @return
     */
    public Map computeIterationAc(int itid, LocalDate finishdate) {
        // 当前迭代
        Iteration iteration = iterationRepository.findById(itid).get();

        // 预期时间
        int predictDay = (int) iteration.getBeginTime().until(iteration.getEndTime(), ChronoUnit.DAYS) + 1;
        // 实际时间
        int actualDay = (int) iteration.getBeginTime().until(finishdate, ChronoUnit.DAYS) + 1;

        //连续按时完成次数
        int successCnt = 0;
        //是否有前驱
        if (iteration.getPrevIteration() != 0) {
            // 上一个迭代的连续按时发布数
            successCnt = iterationRepository.getConSucessCntById(iteration.getPrevIteration());
        }


        //迭代人数
        int num = iteration.getIterationDetails().size();
        // 预期AC
        double AcPredict = predictDay * num / 30.0;
        // 实际AC
        double AcActual = actualDay * num / 30.0;

        // AC扣除
        double AcReduce = 0;
        if (predictDay < actualDay)  {
            log.debug("延期交付");
            successCnt = 0;
            int delay = (int) iteration.getEndTime().until(finishdate, ChronoUnit.DAYS);
            if (Math.ceil(delay / 7.0) <= 1.0) {
                AcReduce = Math.ceil(delay / 7.0) * 0.15 * AcPredict;
            } else {
                AcReduce = Math.ceil(delay / 7.0) * 0.25 * AcPredict;
            }
        } else { // 如果没有延期
            successCnt++;
        }

        // AC奖励
        double AcAward = fib(successCnt);

        double[] dclist = new double[num];

        //  开始年月周
        int begin = utils.getTimeCode(iteration.getBeginTime());
        // 结束年月周
        int end = utils.getTimeCode(finishdate);


        String period = utils.getTimeStr(iteration.getBeginTime()) + " ~ " + utils.getTimeStr(finishdate);
        String info;
        if (begin == end) {
            info = "迭代在一周之内,有效占比: " + actualDay + "/7 ";
        } else {
            int beginWeek = iteration.getBeginTime().getDayOfWeek().getValue();
            int endWeek = finishdate.getDayOfWeek().getValue();
            info = "第1周有效占比: " + (7 - beginWeek + 1) + "/7   " + "   结束周有效占比: " + endWeek + "/7";
        }

        int index = 0;
        double dcSum = 0;
        for (IterationDetail itd : iteration.getIterationDetails()) {
            double dcAll = dcRecordRepository.getUserDcSumByDate(itd.getUser().getId(), iteration.getAuditor().getId(), begin, end);
            double dcBeginWeek = dcRecordRepository.getUserDcByWeek(itd.getUser().getId(), iteration.getAuditor().getId(), begin);
            double dcEndWeek = dcRecordRepository.getUserDcByWeek(itd.getUser().getId(), iteration.getAuditor().getId(), end);
            double deduct = (dcBeginWeek * (iteration.getBeginTime().getDayOfWeek().getValue() - 1) + dcEndWeek * (7 - finishdate.getDayOfWeek().getValue())) / 7.0;
            // 开发期间周dc均值
            dclist[index] = (dcAll - deduct) * 7.0 / actualDay;
            dcSum += dclist[index];
            index ++;
        }

        double totalAc = AcActual + AcAward - AcReduce;

        if (dcSum == 0) {
            return Map.of("status", false, "AcActual", AcActual, "AcReduce", AcReduce, "AcAward", AcAward, "totalAc",  totalAc);
        }


        index = 0;
        List<IterateInfoVO> iterateInfoVOS = new ArrayList<>();
        for (IterationDetail itd : iteration.getIterationDetails()) {
            // AC 计算公式
            double ac = (AcActual + AcAward - AcReduce) * dclist[index] / dcSum * dclist[index] * 2;
            IterateInfoVO infoVO = new IterateInfoVO(itd.getUser().getName(), dclist[index], ac);
            iterateInfoVOS.add(infoVO);
            index ++;
        }

        return Map.of( "status", true, "info", info, "AcActual", AcActual, "AcReduce", AcReduce, "AcAward", AcAward, "totalAc", totalAc, "period",  period, "dcSum", dcSum, "iterateInfos", iterateInfoVOS);
    }


    /**
     * 计算或更新迭代AC值
     * @param itid
     * @param finishdate
     */
    public void setIterationAc(int itid, LocalDate finishdate) {
        // 当前迭代
        Iteration iteration = iterationRepository.findById(itid).get();
        Project project = iteration.getProject();

        // 预期时间
        int predictDay = (int) iteration.getBeginTime().until(iteration.getEndTime(), ChronoUnit.DAYS) + 1;
        // 实际时间
        int actualDay = (int) iteration.getBeginTime().until(finishdate, ChronoUnit.DAYS) + 1;

        //连续按时完成次数
        int successCnt = 0;
        //是否有前驱
        if (iteration.getPrevIteration() != 0) {
            // 上一个迭代的连续按时发布数
            successCnt = iterationRepository.getConSucessCntById(iteration.getPrevIteration());
        }

        //迭代人数
        int num = iteration.getIterationDetails().size();
        // 预期AC
        double AcPredict = predictDay * num / 30.0;
        // 实际AC
        double AcActual = actualDay * num /30.0;
        // AC扣除
        double AcReduce = 0;
        if (predictDay < actualDay) {
            log.debug("延期交付");
            // 连续奖励中断
            successCnt = 0;
            int delay = (int) iteration.getEndTime().until(finishdate, ChronoUnit.DAYS);
            if (Math.ceil(delay / 7.0) <= 1.0) {
                AcReduce = Math.ceil(delay / 7.0) * 0.15 * AcPredict;
            } else {
                AcReduce = Math.ceil(delay / 7.0) * 0.25 * AcPredict;
            }

        } else { // 如果按时完成，并不是修改迭代结束时间
            log.debug("按时交付");
            // 连续按时交付次数加 1
            successCnt++;
        }
        iteration.setConSuccess(successCnt);
        project.setSuccessCnt(successCnt);

        // AC奖励
        double AcAward = fib(successCnt);


        double[] dclist = new double[num];

        int begin = utils.getTimeCode(iteration.getBeginTime());
        int end = utils.getTimeCode(finishdate);
        int index = 0;
        double dcSum = 0;
        for (IterationDetail itd : iteration.getIterationDetails()) {
            double dcAll = dcRecordRepository.getUserDcSumByDate(itd.getUser().getId(), iteration.getAuditor().getId(), begin, end);
            double dcBeginWeek = dcRecordRepository.getUserDcByWeek(itd.getUser().getId(), iteration.getAuditor().getId(), begin);
            double dcEndWeek = dcRecordRepository.getUserDcByWeek(itd.getUser().getId(), iteration.getAuditor().getId(), end);
            double deduct = (dcBeginWeek * (iteration.getBeginTime().getDayOfWeek().getValue() - 1) + dcEndWeek * (7 - finishdate.getDayOfWeek().getValue())) / 7.0;
            // 开发期间周dc均值
            dclist[index] = (dcAll - deduct) * 7.0 / actualDay;
            dcSum += dclist[index];
            index ++;
        }

        if (dcSum == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "项目参与者的总dc值为0，可能是参与者未提交dc申请，无法计算，需自定义");
        }

        index = 0;
        List<AcRecord> acRecords = new ArrayList<>();
        for (IterationDetail itd : iteration.getIterationDetails()) {
            if (itd.getAcRecord() != null) {
                acRecordRepository.delete(itd.getAcRecord());
            }
            // AC 计算公式
            double ac = (AcActual + AcAward - AcReduce) * dclist[index] /dcSum * dclist[index] * 2;
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

        // 发送消息
        notifyService.autoSetProjectAcMessage(acRecords);
        // 计算助研金
        LocalDate date = LocalDate.now();
        int yearmonth = date.getYear() * 100 + date.getMonthValue();
        for (AcRecord ac : acRecords) {
            performanceService.computeSalary(ac.getUser().getId(), yearmonth);
        }

    }


    /**
     * 自定义项目的ac值
     * @param itid
     * @param iterationDetails
     * @return
     */
    public List<AcRecord> manualSetIterationAc(int itid, List<IterationDetail> iterationDetails, LocalDate finishdate) {
        Iteration iteration = iterationRepository.findById(itid).get();
        //设置完成时间
        iteration.setFinishTime(finishdate);
        Project project = iteration.getProject();
        iteration.setStatus(true);

        //连续按时完成次数
        int successCnt = 0;
        if (iteration.getPrevIteration() != 0) { //是否有前驱
            // 上一个迭代的连续按时发布数
            successCnt = iterationRepository.getConSucessCntById(iteration.getPrevIteration());
        }
        if (iteration.getEndTime().isBefore(finishdate)) {
            successCnt = 0;
        } else {
            successCnt++;
        }

        iteration.setConSuccess(successCnt);
        project.setSuccessCnt(successCnt);

        // 用于发送消息，计算助研金
        List<AcRecord> acRecords = new ArrayList<>();
        for (IterationDetail td : iterationDetails) {
            IterationDetail iterationDetail = iterationDetailRepository.findById(td.getId()).get();
            // 删除之前的 acrecord
            if (iterationDetail.getAcRecord() != null) {
                acRecordRepository.delete(iterationDetail.getAcRecord());
            }
            // 更新AC
            iterationDetail.setAc(td.getAc());
            AcRecord acRecord = new AcRecord(td.getUser(), iteration.getAuditor(), td.getAc(), "完成开发任务: " + project.getTitle() + "第" + iteration.getCnt() + "次迭代", AcRecord.PROJECT);
            acRecordRepository.save(acRecord);
            iterationDetail.setAcRecord(acRecord);
            acRecords.add(acRecord);
        }


        // 发送消息
        notifyService.manualSetProjectAcMessage(acRecords);
        // 计算助研金
        LocalDate date = LocalDate.now();
        int yearmonth = date.getYear() * 100 + date.getMonthValue();
        for (AcRecord ac : acRecords) {
            performanceService.computeSalary(ac.getUser().getId(), yearmonth);
        }

        return acRecords;
    }

}
