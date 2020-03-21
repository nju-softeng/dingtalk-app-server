package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Bug;
import com.softeng.dingtalk.repository.BugRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 用户提交bug
     * @param bug
     */
    public void submitBug(Bug bug) {
        bugRepository.save(bug);
    }


    /**
     * 查询指定项目的bug
     * @param pid
     */
    public List<Bug> listProjectBug(int pid) {
        return bugRepository.findAllByProjectId(pid);
    }


    /**
     * 用户删除bug
     * @param id
     */
    public void rmbug(int id) {
        bugRepository.deleteById(id);
    }

    public List<Bug> listProjectBugByAuditor(int aid) {
        return bugRepository.listBugByAuditor(aid);
    }


    // 审核人确认bug
    public void checkbug() {

    }


}
