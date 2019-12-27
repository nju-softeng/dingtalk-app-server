package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.AcItem;
import com.softeng.dingtalk.entity.Application;
import com.softeng.dingtalk.repository.AcItemRepository;
import com.softeng.dingtalk.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author zhanyeye
 * @description 周绩效申请业务逻辑
 * @create 12/11/2019 2:35 PM
 */
@Service
@Transactional
@Slf4j
public class ApplicationService {
    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    AcItemRepository acItemRepository;

    //添加申请 ->  用户提交一个周绩效申请（包括DC和AC）
    public void addApplication(Application application, List<AcItem> acItems) {
        Application a = applicationRepository.save(application);
        applicationRepository.refresh(a);
        for (int i = 0; i < acItems.size(); i++) { // 持久化ac申请，并将绩效申请作为外键
            acItems.get(i).setApplication(application);
            acItemRepository.save(acItems.get(i));
        }
    }

    //获取指定用户的申请 ->  用于查看申请历史
    public List<Application> getApplications(int uid, int page) {
        Pageable pageable = PageRequest.of(page, 10); //分页对象，每页10个
        return applicationRepository.listApplicationByuid(uid, pageable);
    }



}
