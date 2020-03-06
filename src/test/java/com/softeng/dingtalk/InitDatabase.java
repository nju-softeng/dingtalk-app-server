package com.softeng.dingtalk;

import com.softeng.dingtalk.component.DingTalkUtils;
import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.entity.PaperLevel;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.repository.PaperLevelRepository;
import com.softeng.dingtalk.repository.UserRepository;
import com.softeng.dingtalk.service.AuditService;
import com.softeng.dingtalk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanyeye
 * @description 初始化数据库数据
 * @create 12/28/2019 8:28 PM
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class InitDatabase {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Autowired
    AuditService auditService;
    @Autowired
    UserService userService;
    @Autowired
    DingTalkUtils dingTalkUtils;
    @Autowired
    PaperLevelRepository paperLevelRepository;


    @Test
    public void fetchUsrs() {
        userService.fetchUsers();
    }

    @Test
    public void init() {
        userService.fetchUsers();
        addpaperLevel();

    }

    @Autowired
    public void addpaperLevel() {
        List<PaperLevel> paperLevels = new ArrayList<>();
        paperLevels.add(new PaperLevel("Journal A", 1, 60));
        paperLevels.add(new PaperLevel("Conference A", 2, 50));
        paperLevels.add(new PaperLevel("Journal B", 3, 36));
        paperLevels.add(new PaperLevel("Conference B", 4, 30));
        paperLevels.add(new PaperLevel("Journal C", 5, 24));
        paperLevels.add(new PaperLevel("Conference C", 6, 20));

        paperLevelRepository.saveAll(paperLevels);

    }






    @Test
    public void addTestUser() {
        List<User> users = new ArrayList<>();
//        users.add(new User("00002", "user2", "test", 1 ));
//        users.add(new User("00003", "user3", "test", 1 ));
//        users.add(new User("00004", "user4", "test", 1 ));
//        users.add(new User("00006", "user6", "test", 0 ));
//        users.add(new User("00007", "user7", "test", 0 ));
//        users.add(new User("00008", "user8", "avatar8", 0 ));
//        users.add(new User("00009", "user9", "avatar9", 0 ));
//        users.add(new User("000010", "user10", "avatar10", 0 ));
        userRepository.saveAll(users);
    }



}
