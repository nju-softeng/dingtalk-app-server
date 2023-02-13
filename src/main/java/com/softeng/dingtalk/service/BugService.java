package com.softeng.dingtalk.service;

import com.softeng.dingtalk.po.*;
import com.softeng.dingtalk.dao.repository.AcRecordRepository;
import com.softeng.dingtalk.dao.repository.BugDetailRepository;
import com.softeng.dingtalk.dao.repository.BugRepository;
import com.softeng.dingtalk.dao.repository.IterationDetailRepository;
import com.softeng.dingtalk.vo.BugCheckVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 3/8/2020 11:26 PM
 */
@Service
@Transactional
@Slf4j
public class BugService {
    @Autowired
    BugRepository bugRepository;
    @Autowired
    IterationDetailRepository iterationDetailRepository;
    @Autowired
    BugDetailRepository bugDetailRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    NotifyService notifyService;
    @Autowired
    PerformanceService performanceService;


    /**
     * 用户提交bug
     * @param bug
     */
    public void submitBug(BugPo bug) {
        bugRepository.save(bug);
    }


    /**
     * 查询指定项目的bug
     * @param pid
     * @return
     */
    public List<BugPo> listProjectBug(int pid) {
        return bugRepository.findAllByProjectId(pid);
    }


    /**
     * 用户删除bug
     * @param id
     */
    public void rmbug(int id) {
        BugPo bug = bugRepository.findById(id).get();
        if (bug.getStatus() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bug 已处理,无法删除");
        }
        bugRepository.deleteById(id);
    }


    public List<BugPo> listProjectBugByAuditor(int aid) {
        return bugRepository.listBugByAuditor(aid);
    }


    /**
     * 审核人确认bug
     * @param vo
     */
    public void checkbug(BugCheckVO vo) {
        bugDetailRepository.deleteBugDetailByBugId(vo.getId());
        if (!vo.isStatus()) {
            // bug 不存在
            bugRepository.updateBugStatus(vo.getId(), false);
        } else {
            // 存在bug
            BugPo bug = bugRepository.findById(vo.getId()).get();
            bug.setStatus(true);
            // 审核人
            UserPo auditor = bug.getProject().getAuditor();
            // bug 所属迭代的所有用户
            List<UserPo> userPos = iterationDetailRepository.listUserByIterationId(vo.getIterationId());
            List<BugDetailPo> bugDetailPos = new ArrayList<>();
            List<AcRecordPo> acRecordPos = new ArrayList<>();
            //迭代参与人数
            int cnt = userPos.size();

            double ac;
            AcRecordPo acRecordPO;
            String reason;
            for (UserPo u : userPos) {
                if (u.getId() != vo.getUid()) {
                    ac = - 0.1 / (cnt -1);
                    reason = "开发任务： " +  bug.getProject().getTitle() + " 存在bug, 非主要负责人";
                } else {
                    ac = - 0.1;
                    reason = "开发任务： " +  bug.getProject().getTitle() + " 存在bug, 为主要负责人";
                }
                acRecordPO = new AcRecordPo(u, auditor, ac, reason, AcRecordPo.BUG, bug.getInsertTime());
                acRecordPos.add(acRecordPO);
                BugDetailPo bugDetailPo = new BugDetailPo(new BugPo(vo.getId()), u, false, ac, acRecordPO);
                bugDetailPos.add(bugDetailPo);
            }
            acRecordRepository.saveAll(acRecordPos);
            bugDetailRepository.saveAll(bugDetailPos);

            // 发送消息
            notifyService.bugMessage(acRecordPos);

            // 更新绩效
            acRecordPos.forEach(a -> performanceService.computeSalary(a.getUser().getId(), LocalDate.now()));

        }
    }


    /**
     * 查询指定用户的 bug
     * @param uid
     * @return
     */
    public List<BugPo> listUserBug(int uid) {
        List<Integer> ids = bugDetailRepository.listBugidByuid(uid);
        if (ids.size() != 0) {
            return bugRepository.findAllById(ids);
        } else {
            return null;
        }
    }


    /**
     * 审核人待审核bug数
     * @param uid
     * @return
     */
    public int getAuditorBugCnt(int uid) {
        return bugRepository.getAuditorPendingBugCnt(uid);
    }

}
