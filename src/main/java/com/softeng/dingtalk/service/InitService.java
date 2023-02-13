package com.softeng.dingtalk.service;

import com.softeng.dingtalk.dao.repository.*;
import com.softeng.dingtalk.po.DcSummaryPo;
import com.softeng.dingtalk.po.PaperLevelPo;
import com.softeng.dingtalk.po.PatentLevelPo;
import com.softeng.dingtalk.po.SubsidyLevelPo;
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
            List<PaperLevelPo> paperLevelPos = new ArrayList<>();
            paperLevelPos.add(new PaperLevelPo("Journal A", PaperType.JOURNAL_A, 60));
            paperLevelPos.add(new PaperLevelPo("Conference A", PaperType.CONFERENCE_A, 50));
            paperLevelPos.add(new PaperLevelPo("Journal B", PaperType.JOURNAL_B, 36));
            paperLevelPos.add(new PaperLevelPo("Conference B", PaperType.CONFERENCE_B, 30));
            paperLevelPos.add(new PaperLevelPo("Journal C", PaperType.JOURNAL_C, 24));
            paperLevelPos.add(new PaperLevelPo("Conference C", PaperType.CONFERENCE_C, 20));
            paperLevelRepository.saveAll(paperLevelPos);
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
            List<SubsidyLevelPo> subsidyLevelPos = new ArrayList<>();
            subsidyLevelPos.add(new SubsidyLevelPo(Position.DOCTOR, 250));
            subsidyLevelPos.add(new SubsidyLevelPo(Position.ACADEMIC, 200));
            subsidyLevelPos.add(new SubsidyLevelPo(Position.PROFESSIONAL, 200));
            subsidyLevelPos.add(new SubsidyLevelPo(Position.UNDERGRADUATE, 0));
            subsidyLevelPos.add(new SubsidyLevelPo(Position.OTHER, 0));
            subsidyLevelRepository.saveBatch(subsidyLevelPos);
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
                .map(id -> new DcSummaryPo(id, yearmonth))
                .collect(Collectors.toList())
        );
    }

    public void initPatentLevel(){
        if(patentLevelRepository.count()==0){
            PatentLevelPo patentLevelPo =new PatentLevelPo();
            patentLevelPo.setTitle("patent");
            patentLevelPo.setTotal(40);
            patentLevelRepository.save(patentLevelPo);
        }
    }


}
