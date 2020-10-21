package com.softeng.dingtalk.service;


import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.mapper.PaperMapper;

import com.softeng.dingtalk.repository.*;

import com.softeng.dingtalk.vo.AuthorVO;
import com.softeng.dingtalk.vo.PaperInfoVO;
import com.softeng.dingtalk.vo.PaperVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    InternalVoteRepository internalVoteRepository;
    @Autowired
    NotifyService notifyService;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    PaperMapper paperMapper;


    /**
     * 添加论文记录
     * @param vo
     */
    public void addPaper(PaperVO vo) {
        Paper paper = new Paper(vo.getTitle(), vo.getJournal(), vo.getPaperType(), vo.getIssueDate());
        paperRepository.save(paper);
        List<PaperDetail> paperDetails = new ArrayList<>();
        for (AuthorVO author : vo.getAuthors()) {
            PaperDetail pd = new PaperDetail(paper, new User(author.getUid()), author.getNum());
            paperDetails.add(pd);
        }
        paperDetailRepository.saveBatch(paperDetails);
    }


    /**
     * 更新论文记录
     * @param paperVO
     */
    @CacheEvict(value =  "authors_id" , key = "#paperVO.id")
    public void updatePaper(PaperVO paperVO) {
        Paper paper = paperRepository.findById(paperVO.getId()).get();
        paper.update(paperVO.getTitle(), paperVO.getJournal(), paperVO.getPaperType(), paperVO.getIssueDate());
        // 更新
        paperRepository.save(paper);
        // 删除旧的paperDetail
        paperDetailRepository.deleteByPaper(paper);
        // 重新添加paperDetail
        List<PaperDetail> paperDetails = new ArrayList<>();
        for (AuthorVO author : paperVO.getAuthors()) {
            PaperDetail pd = new PaperDetail(paper, new User(author.getUid()), author.getNum());
            paperDetails.add(pd);
        }
        paperDetailRepository.saveBatch(paperDetails);
    }


    /**
     * 删除论文
     * @param id
     */
    public void deletePaper(int id) {
        paperDetailRepository.deleteByPaper(new Paper(id));
        paperRepository.deleteById(id);
        reviewRepository.deleteByPaperid(id);
    }


    /**
     * 更新论文结果, 并计算ac
     * @param id
     * @param result
     */
    public void updateResult(int id, boolean result) {

        Paper paper = paperRepository.findById(id).get();

        if (paper.getInternalVote().getResult() == null || paper.getInternalVote().getResult() == false) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "内审投票未结束或未通过！");
        }
        //更新指定 论文的结果
        if (result == true) {
            paper.setResult(Paper.ACCEPT);
        } else {
            paper.setResult(Paper.REJECT);
        }

        paperRepository.save(paper);

        // 计算AC
        //获取论文奖励总AC
        double sum = paperLevelRepository.getvalue(paper.getPaperType());
        String reason = paper.getTitle();
        if (result == false) {
            //如果被拒绝则扣分
            sum *= -0.5;
            reason += " Reject";
        } else {
            reason += " Accept";
        }
        //获取论文参与者
        List<PaperDetail> paperDetails = paperDetailRepository.findByPaper(new Paper(id));

        List<AcRecord> oldacRecords = paperDetails.stream().filter(x-> x.getAcRecord() != null).map(x-> x.getAcRecord()).collect(Collectors.toList());
        acRecordRepository.deleteAll(oldacRecords);

        double[] rate = new double[]{0.5, 0.25, 0.15, 0.1};
        int i = 0;
        for (PaperDetail pd : paperDetails) {
            if (i == 4) {
                break;
            }
            double ac = sum * rate[pd.getNum() - 1];
            AcRecord acRecord = new AcRecord(pd.getUser(), null, ac, reason, AcRecord.PAPER);
            pd.setAc(ac);
            pd.setAcRecord(acRecord);
            acRecordRepository.save(acRecord);
            i++;
        }
        paperDetailRepository.saveAll(paperDetails);
        // 发送消息
        notifyService.paperAcMessage(id, result);
        // 计算助研金
        LocalDate date = LocalDate.now();
        int yearmonth = date.getYear() * 100 + date.getMonthValue();
        for (PaperDetail pd : paperDetails) {
            performanceService.computeSalary(pd.getUser().getId(), yearmonth);
        }

    }


    /**
     * 分页查看论文
     * @param page
     * @return
     */
    public Map listPaper(int page, int size) {
        int offset = (page - 1) * size;
        List<PaperInfoVO> paperlist = paperMapper.listPaperInfo(offset, size);
        int total = paperMapper.countPaper();
        return Map.of("list", paperlist, "total", total);
    }


    /**
     * 获取论文的详细信息
     * @param id
     * @return
     */
    public Paper getPaper(int id) {
        Paper paper = paperRepository.findById(id).get();
        paper.setPaperDetails(paperDetailRepository.findByPaper(paper));
        return paper;
    }


    /**
     * 获取论文对应的投票
     * @param pid
     * @return
     */
    public InternalVote getVoteByPid (int pid) {
        return paperRepository.findVoteById(pid);
    }


    /**
     * 提交论文评审建议
     * @param review
     * @param uid
     * @return
     */
    public void submitReview(Review review, int uid) {
        User user = new User(uid);
        review.setUser(user);
        reviewRepository.save(review);
    }


    /**
     * 查询指定论文的评审意见
     * @param paperid
     * @return
     */
    public List<Review> listReview(int paperid) {
        return reviewRepository.findAllByPaperid(paperid, Sort.by("id").descending());
    }


    /**
     * 更新评审意见
     * @param review
     */
    public void updateReview(Review review) {
       reviewRepository.save(review);
       log.debug(review.getUpdateTime().toString());
    }


    /**
     * 删除评审意见
     * @param id
     * @param uid
     */
    public void deleteReview(int id, int uid) {
        Review review = reviewRepository.findById(id).get();
        if (uid != review.getUser().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "无删除权限");
        }
        reviewRepository.deleteById(id);
    }


    /**
     * 查询指定论文的作者id
     * 缓存查询的结果
     * @param pid
     * @return
     */
    @Cacheable(value = "authors_id", key = "#pid")
    public Set<Integer> listAuthorid(int pid) {
        return paperDetailRepository.listAuthorIdByPid(pid);
    }



}
