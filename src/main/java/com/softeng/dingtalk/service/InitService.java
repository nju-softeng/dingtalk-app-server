package com.softeng.dingtalk.service;

import com.softeng.dingtalk.dao.repository.*;
import com.softeng.dingtalk.entity.DcSummary;
import com.softeng.dingtalk.entity.PaperLevel;
import com.softeng.dingtalk.entity.PatentLevel;
import com.softeng.dingtalk.entity.SubsidyLevel;
import com.softeng.dingtalk.enums.PaperType;
import com.softeng.dingtalk.enums.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Autowired
    PatentLevelRepository patentLevelRepository;

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
            subsidyLevels.add(new SubsidyLevel(Position.ACADEMIC, 200));
            subsidyLevels.add(new SubsidyLevel(Position.PROFESSIONAL, 200));
            subsidyLevels.add(new SubsidyLevel(Position.UNDERGRADUATE, 0));
            subsidyLevels.add(new SubsidyLevel(Position.OTHER, 0));
            subsidyLevels.add(new SubsidyLevel(Position.TEACHER, 0));
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
        dcSummaryRepository.saveBatch(total.stream()
                .map(id -> new DcSummary(id, yearmonth))
                .collect(Collectors.toList())
        );
    }

    public void initPatentLevel(){
        if(patentLevelRepository.count()==0){
            PatentLevel patentLevel =new PatentLevel();
            patentLevel.setTitle("patent");
            patentLevel.setTotal(40);
            patentLevelRepository.save(patentLevel);
        }
    }


}
