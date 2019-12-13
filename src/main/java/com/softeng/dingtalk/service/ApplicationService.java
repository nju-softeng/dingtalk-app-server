package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Application;
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

    //添加申请 ->  用户提交一个绩效申请
    public Application addApplication(Application application) {
        Application a = applicationRepository.save(application);
        return applicationRepository.refresh(a);
    }

    //获取指定用户的申请 ->  用于查看申请历史
    public List<Application> getApplications(int uid, int page) {
        Pageable pageable = PageRequest.of(page, 10); //分页对象，每页10个
        return applicationRepository.listApplicationByuid(uid, pageable);
    }

//    //根据uid(审核人)，获得待审核的申请  ->  审核人查看待审核的申请
//    public List<Application> getPendingApplication(int uid) {
//        return applicationRepository.listPendingApplication(uid);
//    }


}
