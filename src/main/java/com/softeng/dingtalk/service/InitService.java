package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.entity.PaperLevel;
import com.softeng.dingtalk.entity.SubsidyLevel;
import com.softeng.dingtalk.enums.Position;
import com.softeng.dingtalk.repository.PaperLevelRepository;
import com.softeng.dingtalk.repository.SubsidyLevelRepository;
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
    private SubsidyLevelRepository subsidyLevelRepository;
    @Autowired
    private SystemService systemService;

    /**
     * 初始化论文级别信息
     */
    public void initPaperLevel() {
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

    /**
     * 初始化系统用户
     */
    public void initUser() {
        systemService.fetchUsers();
    }

    /**
     * 初始化绩效基准
     */
    public void initSubsidyLevel() {
        if (subsidyLevelRepository.count() == 0) {
            List<SubsidyLevel> subsidyLevels = new ArrayList<>();
            subsidyLevels.add(new SubsidyLevel(Position.DOCTOR, 250));
            subsidyLevels.add(new SubsidyLevel(Position.POSTGRADUATE, 150));
            subsidyLevels.add(new SubsidyLevel(Position.UNDERGRADUATE, 0));
            subsidyLevels.add(new SubsidyLevel(Position.OTHER, 0));
            subsidyLevelRepository.saveBatch(subsidyLevels);
        }
    }

}
