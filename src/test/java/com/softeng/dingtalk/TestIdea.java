package com.softeng.dingtalk;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.component.Utils;
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
import java.util.concurrent.ExecutionException;


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


    @Test
    public void test_sync() throws ExecutionException, InterruptedException {
        userService.fetchUsers();
    }

    @Test
    public void test_1() {

    }

    @Test
    public void test_2() {
        dingTalkUtils.sendVoteResult("测试标题", true, 25, 40);
    }


    @Test
    public void test() {
        List<String> name = new ArrayList<>();
        name.add("zhanzeye");
        name.add("caoxiaojun");
        name.add("dakaixin");
        dingTalkUtils.sendVoteMsg(1,"papertitle", "16:00", name);
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
