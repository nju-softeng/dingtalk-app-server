package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.projection.PaperProjection;
import com.softeng.dingtalk.repository.*;

import com.softeng.dingtalk.vo.PaperVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhanyeye
 * @description
 * @create 2/5/2020 9:15 PM
 */
@Service
@Transactional
@Slf4j
public class PaperService {
    @Autowired
    PaperRepository paperRepository;
    @Autowired
    PaperDetailRepository paperDetailRepository;
    @Autowired
    PaperLevelRepository paperLevelRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    VoteRepository voteRepository;

    // 添加论文记录
    public void addPaper(PaperVO papervo) {
        Paper paper = new Paper(papervo);
        paperRepository.save(paper);
        for (PaperDetail pd : papervo.getPaperDetails()) {
            pd.setPaper(paper);
        }
        paperDetailRepository.saveAll(papervo.getPaperDetails());
    }


    // 更新论文记录
    public void updatePaper(PaperVO paperVO) {
        Paper paper = paperRepository.findById(paperVO.getId()).get();
        paper.update(paperVO.getTitle(), paperVO.getJournal(), paperVO.getLevel(), paperVO.getIssueDate());
        paperRepository.save(paper); //更新
        paperDetailRepository.deleteByPaper(paper); // 删除paperDetail
        for (PaperDetail pd : paperVO.getPaperDetails()) { // 重新添加paperDetail
            pd.setPaper(paper);
        }
        paperDetailRepository.saveAll(paperVO.getPaperDetails());

        if (paper.getResult() != null) {
            updateResult(paper.getId(), paper.getResult());
        }
    }


    // 删除论文
    public void deletePaper(int id) {
        // paperDetailRepository.deleteByPaperid(id);
        paperDetailRepository.deleteByPaper(new Paper(id));
        paperRepository.deleteById(id);
    }


    // 更新论文结果, 并计算ac
    public void  updateResult(int id, boolean result) {

        Paper paper = paperRepository.findById(id).get();
        paper.setResult(result);    //更新指定 论文的结果
        paperRepository.save(paper);

        // 计算AC
        double sum = paperLevelRepository.getvalue(paper.getLevel()); //获取论文奖励总AC
        String reason = paper.getTitle();
        if (result == false) { //如果被拒绝则扣分
            sum *= -0.5;
            reason += " Reject";
        } else {
            reason += " Accept";
        }
        List<PaperDetail> paperDetails = paperDetailRepository.findByPaper(new Paper(id)); //获取论文参与者

        List<AcRecord> oldacRecords = paperDetails.stream().filter(x-> x.getAcRecord() != null).map(x-> x.getAcRecord()).collect(Collectors.toList());
        acRecordRepository.deleteAll(oldacRecords);

        double[] rate = new double[]{0.5, 0.25, 0.15, 0.1};
        int i = 0;
        for (PaperDetail pd : paperDetails) {
            if (i == 4) break;
            double ac = sum * rate[pd.getNum() - 1];
            AcRecord acRecord = new AcRecord(pd.getUser(), null, ac, reason);
            pd.setAc(ac);
            pd.setAcRecord(acRecord);
            acRecordRepository.save(acRecord);
            i++;
        }
        paperDetailRepository.saveAll(paperDetails);
    }


    // 分页查看论文
    public Map listPaper(int page) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by("id").descending());
        Page<Integer> pages = paperRepository.listAllId(pageable); //查询出的分页数据对象id
        List<Integer> ids = pages.getContent();
        if (ids.size() != 0) {
            return Map.of("content", paperRepository.findAllById(ids), "total", pages.getTotalElements());
        } else {
            return Map.of("total",0);
        }

    }


    // 获取论文的详细信息
    public Paper getPaper(int id) {
        Paper paper = paperRepository.findById(id).get();
        paper.setPaperDetails(paperDetailRepository.findByPaper(paper));
        return paper;
    }


    // 获取论文对应的投票
    public Vote getVoteByPid (int pid) {
        return paperRepository.findVoteById(pid);
    }




}
