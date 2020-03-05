package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.entity.PaperLevel;
import com.softeng.dingtalk.repository.PaperLevelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @create 3/3/2020 1:56 PM
 */
@Service
@Transactional
@Slf4j
public class InitService {
    @Autowired
    private PaperLevelRepository paperLevelRepository;
    @Autowired
    private UserService userService;

    void initPaperLevel() {
        if (paperLevelRepository.count() == 0) {
            List<PaperLevel> paperLevels = new ArrayList<>();
            paperLevels.add(new PaperLevel("Journal A", 1, 60));
            paperLevels.add(new PaperLevel("Conference A", 2, 50));
            paperLevels.add(new PaperLevel("Journal B", 3, 36));
            paperLevels.add(new PaperLevel("Conference B", 4, 30));
            paperLevels.add(new PaperLevel("Journal C", 5, 24));
            paperLevels.add(new PaperLevel("Conference C", 6, 20));
            paperLevelRepository.saveAll(paperLevels);
        }

    }

    void initUser() {
        userService.fetchUsers();
    }


}
