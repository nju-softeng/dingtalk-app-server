package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Application;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 12/12/2019 8:21 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ApplicationServiceTest {
    @Autowired
    ApplicationRepository applicationRepository;

    @Test
    public void test_findApplication() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Application> applications = applicationRepository.listApplicationByuid(2, pageable);
        for (int i = 0; i < applications.size(); i++) {
            log.debug(applications.get(i).getDc() + "");
        }
    }

    @Test
    public void test() {
        applicationRepository.save(new Application(1, 1, 1));
    }


//    @Test
//    public void test_listPendingApplication() {
//        List<Application> applications = applicationRepository.listPendingApplication(2);
//        for (int i = 0; i < applications.size(); i++) {
//            log.debug(applications.get(i).getApplicant().getName() + "");
//        }
//    }


}
