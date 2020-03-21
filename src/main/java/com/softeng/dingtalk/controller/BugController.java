package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.Bug;
import com.softeng.dingtalk.repository.BugRepository;
import com.softeng.dingtalk.service.BugService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 3/12/2020 6:40 PM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class BugController {
    @Autowired
    BugService bugService;
    @Autowired
    BugRepository bugRepository;

    @PostMapping("/bug")
    public void submitBug(@RequestAttribute int uid, @RequestBody Bug bug) {
        bug.setReporterid(uid);
        bugService.submitBug(bug);
    }

    @GetMapping("/bug/project/{pid}")
    public List<Bug> listProjectBug(@PathVariable int pid) {
        return bugService.listProjectBug(pid);
    }


    @GetMapping("/bug/auditor/{aid}")
    public List<Bug> listProjectBugByAuditor(@PathVariable int aid) {
        return bugService.listProjectBugByAuditor(aid);
    }



}
