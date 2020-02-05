package com.softeng.dingtalk.service;

import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.entity.PaperDetail;
import com.softeng.dingtalk.repository.PaperDetailRepository;
import com.softeng.dingtalk.repository.PaperRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    // 添加论文记录
    public void addPaper(Paper paper) {
        paperRepository.save(paper);
        for (PaperDetail pd : paper.getPaperDetails()) {
            pd.setPaper(paper);
        }
        paperDetailRepository.saveAll(paper.getPaperDetails());
    }

    // 更新论文记录
    public void updatePaper(Paper paper) {
        paperRepository.save(paper);
        paperDetailRepository.deleteByPaper(paper);
        for (PaperDetail pd : paper.getPaperDetails()) {
            pd.setPaper(paper);
        }
        paperDetailRepository.saveAll(paper.getPaperDetails());
    }


    public void  updateResult(int id, int result) {
        paperRepository.updatePaperResult(id, result);
        for ()
    }




}
