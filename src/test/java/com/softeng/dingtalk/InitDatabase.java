package com.softeng.dingtalk;

import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.DcRecordRepository;
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


    @Test
    public void fetchUsrs() {
        userService.fetchUsers();
    }

    @Test
    public void init() {
        addUser();
        ApplicantaddDcRecord();
    }

    @Test
    public void addUser() {
        List<User> users = new ArrayList<>();
        users.add(new User("306147243334957616", "詹泽晔", "https://static-legacy.dingtalk.com/media/lADPDgQ9rK52m0PNAi3NAgc_519_557.jpg", 1 ));
        users.add(new User("00002", "user2", "test", 1 ));
        users.add(new User("00003", "user3", "test", 1 ));
        users.add(new User("00004", "user4", "test", 1 ));
        users.add(new User("315448673626165392", "曹晓俊", null, 0 ));
        users.add(new User("00006", "user6", "test", 0 ));
        users.add(new User("00007", "user7", "test", 0 ));
        users.add(new User("00008", "user8", "avatar8", 0 ));
        users.add(new User("00009", "user9", "avatar9", 0 ));
        users.add(new User("000010", "user10", "avatar10", 0 ));
        userRepository.saveAll(users);
    }

    @Test
    public void ApplicantaddDcRecord() {
        List<DcRecord> dcRecords = new ArrayList<>();
        dcRecords.add(new DcRecord(5, 1, 0.6, 202001, 1));
        dcRecords.add(new DcRecord(5, 2, 0.6, 202001, 1));
        dcRecords.add(new DcRecord(5, 3, 0.6, 202001, 1));
        dcRecords.add(new DcRecord(5, 4, 0.6, 202001, 1));
        dcRecords.add(new DcRecord(6, 1, 0.6, 201912, 5));
        dcRecords.add(new DcRecord(6, 2, 0.6, 201912, 5));
        dcRecords.add(new DcRecord(6, 3, 0.6, 201912, 5));
        dcRecords.add(new DcRecord(6, 4, 0.6, 201912, 5));
        dcRecordRepository.saveAll(dcRecords);
    }


}
