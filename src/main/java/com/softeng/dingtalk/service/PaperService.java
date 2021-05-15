package com.softeng.dingtalk.service;


import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.mapper.InternalPaperMapper;

import com.softeng.dingtalk.repository.*;

import com.softeng.dingtalk.vo.AuthorVO;
import com.softeng.dingtalk.vo.PaperInfoVO;
import com.softeng.dingtalk.vo.PaperVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    InternalPaperRepository internalPaperRepository;
    @Autowired
    PaperDetailRepository paperDetailRepository;
    @Autowired
    PaperLevelRepository paperLevelRepository;
    @Autowired
    AcRecordRepository acRecordRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    NotifyService notifyService;
    @Autowired
    PerformanceService performanceService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    InternalPaperMapper internalPaperMapper;
    @Autowired
    ExternalPaperRepository externalPaperRepository;


    /**
     * 添加论文记录
     * @param vo
     */
    public void addPaper(PaperVO vo) {
        InternalPaper internalPaper = new InternalPaper(vo.getTitle(), vo.getJournal(), vo.getPaperType(), vo.getIssueDate());
        internalPaperRepository.save(internalPaper);
        List<PaperDetail> paperDetails = new ArrayList<>();
        for (AuthorVO author : vo.getAuthors()) {
            PaperDetail pd = new PaperDetail(internalPaper, new User(author.getUid()), author.getNum());
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
        InternalPaper internalPaper = internalPaperRepository.findById(paperVO.getId()).get();
        internalPaper.update(paperVO.getTitle(), paperVO.getJournal(), paperVO.getPaperType(), paperVO.getIssueDate());
        // 更新
        internalPaperRepository.save(internalPaper);
        // 删除旧的paperDetail
        paperDetailRepository.deleteByInternalPaper(internalPaper);
        // 重新添加paperDetail
        List<PaperDetail> paperDetails = new ArrayList<>();
        for (AuthorVO author : paperVO.getAuthors()) {
            PaperDetail pd = new PaperDetail(internalPaper, new User(author.getUid()), author.getNum());
            paperDetails.add(pd);
        }
        paperDetailRepository.saveBatch(paperDetails);
    }


    /**
     * 删除论文
     * @param id
     */
    public void deletePaper(int id) {
        paperDetailRepository.deleteByInternalPaper(new InternalPaper(id));
        internalPaperRepository.deleteById(id);
        reviewRepository.deleteByPaperid(id);
    }

    /**
     * 更新内部论文投稿结果, 并计算ac
     * @param id
     * @param result
     * @param updateDate
     */
    public void updatePaperResult(int id, boolean result, LocalDate updateDate) {

        InternalPaper internalPaper = internalPaperRepository.findById(id).get();

        if (internalPaper.getVote().getResult() == null || internalPaper.getVote().getResult() == false) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "内审投票未结束或未通过！");
        }

        //更新指定 论文的结果
        internalPaper.setResult(result ? InternalPaper.ACCEPT : InternalPaper.REJECT);
        internalPaper.setUpdateDate(updateDate);
        internalPaperRepository.save(internalPaper);

        // 计算AC
        //获取论文奖励总AC
        double sum = paperLevelRepository.getvalue(internalPaper.getPaperType());
        String reason = internalPaper.getTitle();
        if (result == false) {
            //如果被拒绝则扣分
            sum *= -0.5;
            reason += " Reject";
        } else {
            reason += " Accept";
        }
        //获取论文参与者
        List<PaperDetail> paperDetails = paperDetailRepository.findByInternalPaper(new InternalPaper(id));

        List<AcRecord> oldacRecords = paperDetails.stream().filter(x-> x.getAcRecord() != null).map(x-> x.getAcRecord()).collect(Collectors.toList());
        acRecordRepository.deleteAll(oldacRecords);

        double[] rate = new double[]{0.5, 0.25, 0.15, 0.1};
        int i = 0;
        for (PaperDetail pd : paperDetails) {
            double ac = 0;
            if (i >= 4) {
                ac = sum * 0.1;
            } else {
                ac = sum * rate[pd.getNum() - 1];
            }
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
        List<PaperInfoVO> paperlist = internalPaperMapper.listInternalPaperInfo(offset, size);
        int total = internalPaperMapper.countPaper();
        return Map.of("list", paperlist, "total", total);
    }


    /**
     * 获取论文的详细信息
     * @param id
     * @return
     */
    public InternalPaper getPaper(int id) {
        InternalPaper internalPaper = internalPaperRepository.findById(id).get();
        internalPaper.setPaperDetails(paperDetailRepository.findByInternalPaper(internalPaper));
        return internalPaper;
    }


    /**
     * 获取论文对应的投票
     * @param pid
     * @return
     */
    public Vote getVoteByPid (int pid) {
        return internalPaperRepository.findVoteById(pid);
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
     *
     * @param paperid
     * @param isExternal
     * @return
     */
    public List<Review> listReview(int paperid, boolean isExternal) {
        return reviewRepository.findAllByPaperidAndExternal(paperid, isExternal);
    }


    /**
     * 更新评审意见
     * @param review 被更新的评审细节
     */
    public void updateReview(Review review) {
       reviewRepository.save(review);
       log.debug(review.getUpdateTime().toString());
    }


    /**
     * 删除评审意见
     * @param id 评审的id
     * @param uid 要删除的用户的id
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

    //-----------------------------------------

    /**
     * todo 添加分页功能
     * 查询所有外部论文
     * @return
     */
    public List<ExternalPaper> listExternalPaper() {
        return externalPaperRepository.findAll();
    }

    /**
     * 删除指定外部论文
     * @param id
     */
    public void deleteExternalPaper(int id) {
        externalPaperRepository.deleteById(id);
    }


    /**
     * 根据指定id 查询外部论文的投票
     * @param id
     * @return
     */
    public Vote getExPaperVote(int id) {
        return externalPaperRepository.findById(id).get().getVote();
    }


    /**
     * 根据指定id 查询外部评审论文
     * @param id
     * @return
     */
    public ExternalPaper getExPaper(int id) {
        return externalPaperRepository.findById(id).get();
    }


    /**
     * 更新外部论文投稿结果
     * @param id
     * @param result
     * @param updateDate
     */
    public void updateExPaperResult(int id, boolean result, LocalDate updateDate) {
        ExternalPaper externalPaper = externalPaperRepository.findById(id).get();

        if (externalPaper.getVote().getResult() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "投票尚未结束");
        }

        //更新论文的结果
        externalPaper.setResult(result);
        externalPaper.setUpdateDate(updateDate);
        externalPaperRepository.save(externalPaper);
    }


}
