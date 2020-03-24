package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.repository.AcRecordRepository;
import com.softeng.dingtalk.repository.BugDetailRepository;
import com.softeng.dingtalk.repository.BugRepository;
import com.softeng.dingtalk.repository.IterationDetailRepository;
import com.softeng.dingtalk.vo.BugCheckVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

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


    // 用户提交bug
    public void submitBug(Bug bug) {
        bugRepository.save(bug);
    }


    // 查询指定项目的bug
    public List<Bug> listProjectBug(int pid) {
        return bugRepository.findAllByProjectId(pid);
    }


    // 用户删除bug
    public void rmbug(int id) {
        Bug bug = bugRepository.findById(id).get();
        if (bug.getStatus() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bug 已处理,无法删除");
        }
        bugRepository.deleteById(id);
    }


    public List<Bug> listProjectBugByAuditor(int aid) {
        return bugRepository.listBugByAuditor(aid);
    }



    // 审核人确认bug
    public void checkbug(BugCheckVO vo) {
        bugDetailRepository.deleteBugDetailByBugId(vo.getId());
        if (vo.isStatus() == false) { // bug 不存在
            bugRepository.updateBugStatus(vo.getId(), false);
        } else { // 存在bug
            Bug bug = bugRepository.findById(vo.getId()).get(); // 当前bug
            bug.setStatus(true);
            User auditor = bug.getProject().getAuditor(); // 审核人
            List<User> users = iterationDetailRepository.listUserByIterationId(vo.getIterationId()); // bug 所属迭代的所有用户
            List<BugDetail> bugDetails = new ArrayList<>();
            List<AcRecord> acRecords = new ArrayList<>();
            int cnt = users.size(); //迭代参与人数

            double ac;
            AcRecord acRecord;
            String reason;
            for (User u : users) {
                if (u.getId() != vo.getUid()) {
                    ac = - 0.1 / (cnt -1);
                    reason = "开发任务： " +  bug.getProject().getTitle() + " 存在bug, 非主要负责人";
                } else {
                    ac = - 0.1;
                    reason = "开发任务： " +  bug.getProject().getTitle() + " 存在bug, 为主要负责人";
                }
                acRecord = new AcRecord(u, auditor, ac, reason, AcRecord.BUG);
                acRecords.add(acRecord);
                BugDetail bugDetail = new BugDetail(new Bug(vo.getId()), u, false, ac, acRecord);
                bugDetails.add(bugDetail);
            }
            acRecordRepository.saveAll(acRecords);
            bugDetailRepository.saveAll(bugDetails);

            // todo 发送消息
        }
    }



    // 查询指定用户的 bug
    public List<Bug> listUserBug(int uid) {
        List<Integer> ids = bugDetailRepository.listBugidByuid(uid);
        if (ids.size() != 0) {
            return bugRepository.findAllById(ids);
        } else {
            return null;
        }
    }


}
