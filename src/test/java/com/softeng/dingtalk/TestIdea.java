package com.softeng.dingtalk;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.component.Timer;
import com.softeng.dingtalk.component.Utils;
import com.softeng.dingtalk.entity.AcRecord;
import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.entity.PaperDetail;
import com.softeng.dingtalk.entity.VoteDetail;
import com.softeng.dingtalk.repository.*;
import com.softeng.dingtalk.service.PaperService;
import com.softeng.dingtalk.service.UserService;
import com.softeng.dingtalk.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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


    @Test
    public void test_sync() throws ExecutionException, InterruptedException {
        int id = userService.getIdByUserid("362554492621552072");
        log.debug(id + "");
    }

    @Test
    public void test_1() {
        Map map = dingTalkUtils.authentication("www.air-dev.cpm");
        log.debug(map.toString());
    }


    @Test
    public void test() {
        List<VoteDetail> voteDetails = new ArrayList<>();
        VoteDetail v1 =  new VoteDetail();
        VoteDetail v2 =  new VoteDetail();
        VoteDetail v3 =  new VoteDetail();
        v1.setAcRecord(new AcRecord());
        v2.setAcRecord(new AcRecord());
        voteDetails.add(v1);
        voteDetails.add(v2);
        voteDetails.add(v3);
        List<AcRecord> acRecords = voteDetails.stream().filter(x -> x.getAcRecord() != null).map((x-> x.getAcRecord())).collect(Collectors.toList());

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
