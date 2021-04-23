package com.softeng.pms.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhanyeye
 * @description
 * @create 3/3/2020 2:24 PM
 */
@Component
public class InitSys implements InitializingBean {

    @Autowired
    private InitService initService;
    @Override
    public void afterPropertiesSet() {
        initService.initPaperLevel();
        initService.initUser();
        initService.initSubsidyLevel();
        initService.initDcSummary();
    }
}
