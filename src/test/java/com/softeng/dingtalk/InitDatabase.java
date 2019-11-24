package com.softeng.dingtalk;

import com.softeng.dingtalk.entity.DcRecord;
import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.repository.DcRecordRepository;
import com.softeng.dingtalk.repository.UserRepository;
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
@SpringBootTest
@Slf4j
public class InitDatabase {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DcRecordRepository dcRecordRepository;
    @Test
    public void init() {
        addUser();
        addDcRecord();
    }

    @Test
    public void addUser() {
        List<User> users = new ArrayList<>();
        users.add(new User("306147243334957616", "詹泽晔", "https://static-legacy.dingtalk.com/media/lADPDgQ9rK52m0PNAi3NAgc_519_557.jpg", 2 ));
        users.add(new User("00002", "user2", "avatar2", 2 ));
        users.add(new User("00003", "user3", "avatar3", 2 ));
        users.add(new User("00004", "user4", "avatar4", 2 ));
        users.add(new User("315448673626165392", "曹晓俊", "avatar5", 0 ));
        users.add(new User("00006", "user6", "avatar6", 0 ));
        users.add(new User("00007", "user7", "avatar7", 0 ));
        users.add(new User("00008", "user8", "avatar8", 0 ));
        users.add(new User("00009", "user9", "avatar9", 0 ));
        users.add(new User("000010", "user10", "avatar10", 0 ));
        userRepository.saveAll(users);
    }

    @Test
    public void addDcRecord() {
        //double dc, LocalDateTime insertTime, int week, User user, User auditor, Application application
        List<DcRecord> dcRecords = new ArrayList<>();
        //dcRecords.add(new DcRecord(0.5, LocalDateTime.of(2019, 12, 1, 0, 0), 1, new User(5),  new User(1)));
        //dcRecords.add(new DcRecord(0.5, LocalDateTime.of(2019, 12, 1, 0, 0), 1, new User(5),  new User(2)));
        dcRecordRepository.saveAll(dcRecords);
    }

}
