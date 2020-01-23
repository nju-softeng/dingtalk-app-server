package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.dto.ApplicationDTO;
import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.service.ApplicationService;
import com.softeng.dingtalk.service.AuditService;
import com.softeng.dingtalk.service.UserService;
import com.softeng.dingtalk.vo.ReportInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 12/11/2019 1:59 PM
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;
    @Autowired
    AuditService auditService;
    @Autowired
    UserService userService;
    @Autowired
    DingTalkUtils dingTalkUtils;
    @Autowired
    Utils utils;

    @PostMapping("/getdate")
    public Map getdate(@RequestBody Map<String, LocalDate> map) {
        log.debug(map.get("time").toString());
        return utils.getTimeFlag(map.get("time"));
    }

//    @PostMapping("/getreport")
//    public Map getReport(@RequestBody ReportInfoVO reportInfoVO) {
//        String userid = userService.getUserid(reportInfoVO.getUid());
//        LocalDateTime dateTime = reportInfoVO.getDateTime();
//        return dingTalkUtils.getReport(userid, dateTime);
//    }


    /**
     * @Description 用户提交申请
     * @Param [applicationInfo]
     * @return void
     * @Date 7:01 PM 12/27/2019
     **/
    @PostMapping("/application")
    public void addApplication(@RequestBody ApplicationDTO applicationDTO, @RequestAttribute int uid) {
        DcRecord dcRecord = applicationDTO.getDcRecord();       //获取DC申请信息
        dcRecord.setApplicant(new User(uid)); // 从请求中获取uid
        List<AcItem> acItems = applicationDTO.getAcItems();     //获取该绩效申请的ac申请
        int aid = dcRecord.getAuditor().getId();
        Map<String, Integer> date = utils.getTimeFlag(applicationDTO.getDate()); //todo 时间判断
        dcRecord.setYearmonth(date.get("yearmonth"));
        dcRecord.setWeek(date.get("week"));
        if (applicationService.isExist(uid, aid, dcRecord.getYearmonth(), dcRecord.getWeek()) == false) {
            applicationService.addApplication(dcRecord, acItems);    //持久化绩效申请
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "每周只能向同一个审核人提交一次申请");
        }
    }


    /**
     * @Description 用户分页查询已提交的申请
     * @Param [uid, page]
     * @return java.util.Map
     * @Date 7:00 PM 12/27/2019
     **/
    @GetMapping("/application/{uid}/page={page}")
    public Map getUserApplication(@RequestAttribute int uid, @PathVariable int page) {
        return applicationService.getDcRecord(uid, page);
        // todo 是否要包含AcItem
    }


}
