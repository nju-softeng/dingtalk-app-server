package com.softeng.pms.service;

import com.softeng.pms.entity.DcSummary;
import com.softeng.pms.entity.PaperLevel;
import com.softeng.pms.entity.SubsidyLevel;
import com.softeng.pms.enums.PaperType;
import com.softeng.pms.enums.Position;
import com.softeng.pms.repository.DcSummaryRepository;
import com.softeng.pms.repository.PaperLevelRepository;
import com.softeng.pms.repository.SubsidyLevelRepository;
import com.softeng.pms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @Autowired
    DcSummaryRepository dcSummaryRepository;
    @Autowired
    UserRepository userRepository;

    /**
     * 初始化论文级别信息
     */
    public void initPaperLevel() {
        if (paperLevelRepository.count() == 0) {
            List<PaperLevel> paperLevels = new ArrayList<>();
            paperLevels.add(new PaperLevel("Journal A", PaperType.JOURNAL_A, 60));
            paperLevels.add(new PaperLevel("Conference A", PaperType.CONFERENCE_A, 50));
            paperLevels.add(new PaperLevel("Journal B", PaperType.JOURNAL_B, 36));
            paperLevels.add(new PaperLevel("Conference B", PaperType.CONFERENCE_B, 30));
            paperLevels.add(new PaperLevel("Journal C", PaperType.JOURNAL_C, 24));
            paperLevels.add(new PaperLevel("Conference C", PaperType.CONFERENCE_C, 20));
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


    /**
     * 每月初始化DcSummary
     */
    public void initDcSummary() {
        LocalDate now = LocalDate.now();
        int yearmonth = now.getYear()*100 + now.getMonthValue();
        Set<Integer> existed = dcSummaryRepository.findIdsByDate(yearmonth);
        Set<Integer> total = userRepository.listUserids();
        total.removeAll(existed);
        List<DcSummary> dcSummaries = new ArrayList<>();
        for (Integer id : total) {
            dcSummaries.add(new DcSummary(id, yearmonth));
        }
        dcSummaryRepository.saveBatch(dcSummaries);
    }


}
