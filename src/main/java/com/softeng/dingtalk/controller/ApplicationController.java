package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.Application;
import com.softeng.dingtalk.repository.AcItemRepository;
import com.softeng.dingtalk.service.AcItemService;
import com.softeng.dingtalk.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    AcItemService acItemService;

    /**
     * @description 添加申请
     * @date 3:02 PM 12/11/2019
     **/
    @PostMapping("/application")
    public void addApplication(@RequestBody Application application) {
        List<AcItem> acItems = application.getAcItems();                   //获取该绩效申请的ac申请
        //拼接 month, week, applicant_id, auditor_id 字段，插入flag约束字段中
        application.setFlag(application.getMonth()+ "-" + application.getWeek() + "-" + application.getApplicant().getId() + "-" + application.getAuditor().getId());
        Application a = applicationService.addApplication(application);    //持久化绩效申请
        acItemService.addAcItemList(acItems, a);                           //持久化ac申请，并将绩效申请作为外键
    }





}
