package com.softeng.dingtalk.service;


import com.softeng.dingtalk.api.BaseApi;
import com.softeng.dingtalk.entity.*;
import com.softeng.dingtalk.mapper.InternalPaperMapper;

import com.softeng.dingtalk.repository.*;

import com.softeng.dingtalk.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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

    @Autowired
    PaperService paperService;

    @Autowired
    BaseApi baseApi;

    /**
     * 根据 internalPaper 和 List<AuthorVO> 生成 PaperDetails
     *
     * @param paper   实验室内部论文
     * @param authors 论文作者VO list
     * @return
     */
    public List<PaperDetail> setPaperDetailsByAuthorsAndPaper(InternalPaper paper, List<AuthorVO> authors) {
        return authors.stream()
                .map(author -> new PaperDetail(paper, author.getUid(), author.getNum()))
                .collect(Collectors.toList());
    }


    /**
     * 添加实验室内部论文
     *
     * @param vo 实验室内部论文VO对象
     */
    public void addInternalPaper(InternalPaperVO vo) {
        InternalPaper internalPaper = new InternalPaper(vo.getTitle(), vo.getJournal(), vo.getPaperType(), vo.getIssueDate(), vo.getIsStudentFirstAuthor(), vo.getFirstAuthor());
        if (!internalPaper.getIsStudentFirstAuthor()) {
            internalPaper.setResult(2);
            internalPaper.setSubmissionFileName(vo.getFileName());
            internalPaper.setSubmissionFileId(vo.getFileId());
        }else {
            internalPaper.setReviewFileName(vo.getFileName());
            internalPaper.setReviewFileId(vo.getFileId());
        }
        internalPaperRepository.save(internalPaper);
        paperDetailRepository.saveBatch(setPaperDetailsByAuthorsAndPaper(internalPaper, vo.getAuthors()));
    }


    /**
     * 添加实验室外部论文
     *
     * @param vo 实验室外部论文vo对象
     */
    public void addExternalPaper(ExternalPaperVO vo) {
        // 创建对应的外部论文对象
        ExternalPaper externalPaper = new ExternalPaper(vo.getTitle());
        externalPaper.setReviewFileName(vo.getFileName());
        externalPaper.setReviewFileId(vo.getFileId());
        externalPaperRepository.save(externalPaper);
        // 创建外部论文对应的投票
        Vote vote = new Vote(vo.getStartTime(), vo.getEndTime(), true, externalPaper.getId());
        externalPaper.setVote(vote);

        voteRepository.save(vote);
    }


    /**
     * 更新内部论文记录
     *
     * @param vo
     */
    public void updateInternalPaper(InternalPaperVO vo) {
        InternalPaper internalPaper = internalPaperRepository.findById(vo.getId()).get();
        // 1. 更新 paper 信息
        internalPaper.update(vo.getTitle(), vo.getJournal(), vo.getPaperType(), vo.getIssueDate(), vo.getFirstAuthor());
        // 2. 删除旧的paperDetail
        paperDetailRepository.deleteByInternalPaper(internalPaper);
        // 3. 插入新的paperDetail
        internalPaper.setPaperDetails(setPaperDetailsByAuthorsAndPaper(internalPaper, vo.getAuthors()));
        // 4. 重新计算ac
        if (internalPaper.hasAccepted() || internalPaper.hasRejected()) {
            paperService.calculateInternalPaperAc(internalPaper);
        }
        // 5. 重新添加paperDetail
        paperDetailRepository.saveBatch(internalPaper.getPaperDetails());
        internalPaperRepository.save(internalPaper);
    }


    /**
     * 更新外部论文记录
     *
     * @param vo
     */
    public void updateExternalPaper(ExternalPaperVO vo) {
        var externalPaper = externalPaperRepository.findById(vo.getId()).get();
        var vote = externalPaperRepository.findVoteById(vo.getId());

        if (vote.getStartTime().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "投票通知已发出,不可以再修改了");
        }

        externalPaper.setTitle(vo.getTitle());
        vote.setStartTime(vo.getStartTime());
        vote.setEndTime(vo.getEndTime());

        externalPaperRepository.save(externalPaper);
        voteRepository.save(vote);
    }


    /**
     * 删除实验室内部论文记录
     *
     * @param id
     */
    public void deleteInternalPaper(int id) {
        paperDetailRepository.deleteByInternalPaper(new InternalPaper(id));
        internalPaperRepository.deleteById(id);
        reviewRepository.deleteByPaperid(id);
    }


    /**
     * 删除实验室外部论文记录
     *
     * @param id
     */
    public void deleteExternalPaper(int id) {
        externalPaperRepository.deleteById(id);
    }

    /**
     * 按照作者排名分配ac值
     * 按照顺序分别比例分别是：0.5， 0.25，0.15，0.1
     * 再后面的作者都按照 0.1 算
     *
     * @param rank 排名
     * @return
     */
    public double calculateRatioOfAc(int rank) {
        switch (rank) {
            case 1:
                return 0.5;
            case 2:
                return 0.25;
            case 3:
                return 0.15;
            default:
                return 0.1;
        }
    }

    /**
     * 计算某个作者的 AC 加减分, 根据论文投稿情况、该论文类型对应的 AC 奖池、作者排名
     *
     * @param isAccept 论文投稿情况
     * @param sum      AC 奖池
     * @param rank     作者排名
     * @return AC值
     */
    public double calculateAc(boolean isAccept, double sum, int rank) {
        return (isAccept ? 1.0 : -0.5) * sum * calculateRatioOfAc(rank);
    }

    /**
     * 计算论文结果对应的 AC
     */
    public void calculateInternalPaperAc(InternalPaper internalPaper) {
        double weight = (internalPaper.getResult() == 6 ? 0.5 : 1); // 如果平票则只计算50%AC
        // 1. 获取 paperDetails
        var paperDetails = internalPaper.getPaperDetails();

        // 2. 删除 paperDetail 对应的旧的 AcRecord. 防止admin后续修改作者信息导致混乱
        acRecordRepository.deleteAll(
                paperDetails.stream()
                        .map(PaperDetail::getAcRecord)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );

        // 3. 查询该类型论文对应的总 AC
        double sum = paperLevelRepository.getValue(internalPaper.getPaperType());

        // 4. 生成 AC 变更原因
        String reason;
        if (weight == 0.5) {
            reason = internalPaper.getTitle() + " Suspend";
        } else {
            reason = internalPaper.getTitle() + (internalPaper.hasAccepted() ? " Accept" : " Reject");
        }

        // 5. 更新 paperDetail 对应的 AcRecord
        paperDetails.forEach(paperDetail -> {
            paperDetail.setAcRecord(new AcRecord(
                    paperDetail.getUser(),
                    null,
                    weight * calculateAc(internalPaper.hasAccepted(), sum, paperDetail.getNum()), // weight等于0.5时表示中止的AC
                    reason,
                    AcRecord.PAPER,
                    internalPaper.getUpdateDate().atTime(8, 0)
            ));
        });

        // 6. 更新paperDetails表和acRecord表
        acRecordRepository.saveAll(
                paperDetails.stream()
                        .map(PaperDetail::getAcRecord)
                        .collect(Collectors.toList())
        );
        paperDetailRepository.saveAll(paperDetails);
    }


    /**
     * todo 需要重构
     * 更新内部论文投稿结果, 并计算ac
     *
     * @param id
     * @param result
     * @param updateDate
     */
    public void updateInternalPaperResult(int id, int result, LocalDate updateDate) {
        // 1. 获取对应的内部论文
        InternalPaper internalPaper = internalPaperRepository.findById(id).get();

        // 2. 校验论文投票和投稿情况
        if (internalPaper.getVote().getResult() == -1 || internalPaper.getVote().getResult() == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "内审投票未结束或未通过！");
        }

        if (internalPaper.getResult() == 5 || internalPaper.getResult() == 6) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "作者未决定投稿或已中止投稿！");
        }

        // 3. 更新指定论文的投稿结果和更新时间
        internalPaper.setResult(result == 1 ? InternalPaper.ACCEPT : InternalPaper.REJECT);
        internalPaper.setUpdateDate(updateDate);
        internalPaperRepository.save(internalPaper);

        // 4. 更新论文 ac
        paperService.calculateInternalPaperAc(internalPaper);

        // 5. 插入相关消息
        notifyService.paperAcMessage(internalPaper);
    }


    /**
     * 更新外部论文投稿结果
     *
     * @param id
     * @param result
     * @param updateDate
     */
    public void updateExPaperResult(int id, int result, LocalDate updateDate) {
        ExternalPaper externalPaper = externalPaperRepository.findById(id).get();

        if (externalPaper.getVote().getResult() == -1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "投票尚未结束");
        }

        //更新论文的结果
        externalPaper.setResult(result);
        externalPaper.setUpdateDate(updateDate);
        externalPaperRepository.save(externalPaper);
    }


    /**
     * @param page
     * @param size
     * @return
     */
    public Page<ExternalPaper> listExternalPaper(int page, int size) {
        return externalPaperRepository.findAll(
                PageRequest.of(
                        page - 1,
                        size,
                        Sort.by("insertTime").descending()
                )
        );
    }

    /**
     * 获取论文的详细信息
     *
     * @param id
     * @return
     */
    public InternalPaper getInternalPaper(int id) {
        InternalPaper internalPaper = internalPaperRepository.findById(id).get();
        internalPaper.setPaperDetails(paperDetailRepository.findByInternalPaper(internalPaper));
        return internalPaper;
    }

    /**
     * 根据指定id 查询外部评审论文
     *
     * @param id
     * @return
     */
    public ExternalPaper getExInternalPaper(int id) {
        return externalPaperRepository.findById(id).get();
    }


    /**
     * 获取论文对应的投票
     *
     * @param pid
     * @return
     */
    public Vote getVoteByPid(int pid) {
        return internalPaperRepository.findVoteById(pid);
    }


    /**
     * 提交论文评审建议
     *
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
     * @param paperId
     * @param isExternal
     * @return
     */
    public List<Review> listReview(int paperId, boolean isExternal) {
        return reviewRepository.findAllByPaperidAndExternal(paperId, isExternal);
    }


    /**
     * 更新评审意见
     *
     * @param review 被更新的评审细节
     */
    public void updateReview(Review review) {
        reviewRepository.save(review);
        log.debug(review.getUpdateTime().toString());
    }


    /**
     * 删除评审意见
     *
     * @param id  评审的id
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
     *
     * @param pid
     * @return
     */
    public Set<Integer> listAuthorId(int pid) {
        return paperDetailRepository.listAuthorIdByPid(pid);
    }


    /**
     * 根据指定id 查询外部论文的投票
     *
     * @param id
     * @return
     */
    public Vote getExPaperVote(int id) {
        return externalPaperRepository.findById(id).get().getVote();
    }

    /**
     * @Description
     * @Author Jerrian Zhao
     * @Data 02/10/2022
     */

    public void decideFlat(FlatDecisionVO flatDecisionVO) {
        InternalPaper internalPaper = internalPaperRepository.findById(flatDecisionVO.getId()).get();
        if (flatDecisionVO.getDecision()) {
            internalPaper.setFlatDecision(1);            internalPaper.setResult(2);
            internalPaperRepository.save(internalPaper);
        } else {
            internalPaper.setFlatDecision(0);
            internalPaper.setResult(6); //投稿中止
            internalPaper.setUpdateDate(LocalDate.now());
            internalPaperRepository.save(internalPaper);
            // 更新论文 ac
            paperService.calculateInternalPaperAc(internalPaper);
            // 插入相关消息
            notifyService.paperAcMessage(internalPaper);
        }
    }

    /**
     * 分页查看内部论文
     *
     * @param page
     * @return
     */
    public Map listInternalPaper(int page, int size) {
        return Map.of(
                "list", internalPaperMapper.listInternalPaperInfo((page - 1) * size, size),
                "total", internalPaperMapper.countPaper()
        );
    }

    /**
     * 分页查看非学生一作
     *
     * @param page
     * @return
     */
    public Map listNonFirstPaper(int page, int size) {
        return Map.of(
                "list", internalPaperMapper.listNonFirstPaperInfo((page - 1) * size, size),
                "total", internalPaperMapper.countNonFirstPaper()
        );
    }
}
