package com.softeng.dingtalk;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.component.Timer;
import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author zhanyeye
 * @description
 * @create 12/29/2019 10:49 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j

public class TestIdea {

    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    Utils utils;
    @Autowired
    DingTalkUtils dingTalkUtils;
    @Autowired
    UserService userService;
    @Autowired
    AcItemRepository acItemRepository;
    @Autowired
    PaperService paperService;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    VoteService voteService;
    @Autowired
    PaperRepository paperRepository;
    @Autowired
    VoteDetailRepository voteDetailRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    PaperDetailRepository paperDetailRepository;
    @Autowired
    Timer timer;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    TopupRepository topupRepository;
    @Autowired
    DcSummaryRepository dcSummaryRepository;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    NotifyService notifyService;
    @Autowired
    ProjectService projectService;

    @Autowired
    BugRepository bugRepository;
    @Autowired
    IterationDetailRepository iterationDetailRepository;

    @Autowired
    UserRepository userRepository;





    @Test
    public void test_delete() {


        Object o =  userService.multiQueryUser("", null);
    }


    @Test
    public void test_1() {
       Object o =  dcRecordRepository.getUserDcByWeek(1, 1, 2020021);
    }

    @Test
    public void test_2() {
       Object o =  iterationDetailRepository.listIterationIdByUid(100);

    }


    @Test
    public void test() {
        Map<String, String> m = new HashMap<>();
        m.put("dateDebut", "2018-07-01T00:00:00.000+0000");
        m.put("nom", "Julien Mannone");
        m.put("etat", "Impayé");

        Map<String, String> m2 = new HashMap<>();
        m2.put("dateDebut", "2018-10-01T00:00:00.000+0000");
        m2.put("nom", "Mathiew Matic");
        m2.put("etat", "payé");

        Map<String, String> m3 = new HashMap<>();
        m3.put("dateDebut", "2018-07-01T00:00:00.000+0000");
        m3.put("nom", "Ash Moon");
        m3.put("etat", "payé");

        List<Map<String, String>> list = new ArrayList<>();
        list.add(m);
        list.add(m2);
        list.add(m3);

        List<Map<String, Map<String, String>>> res = list.stream().map(it -> {
                    Map<String, Map<String, String>> newMap = new HashMap<>();
                    String nom = it.get("nom");
                    it.remove("nom");
                    newMap.put(nom, it);
                    return newMap;
                }
        ).collect(Collectors.toList());

        System.out.println(res);

    }

    @Test
    public void test_listVoteName() {
        List<String> names = voteDetailRepository.listAcceptNamelist(1001);
        log.debug(names.size() + "");
        for (String str : names) {
            log.debug(str);
        }
    }

}
