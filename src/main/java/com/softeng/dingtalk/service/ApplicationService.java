package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Application;
import com.softeng.dingtalk.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    public Application addApplication(Application application) {
        Application a = applicationRepository.save(application);
        return applicationRepository.refresh(a);
    }

}
