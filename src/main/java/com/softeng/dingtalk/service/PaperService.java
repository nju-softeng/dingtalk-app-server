package com.softeng.dingtalk.service;


import com.softeng.dingtalk.api.BaseApi;
import com.softeng.dingtalk.dao.repository.*;
import com.softeng.dingtalk.po.*;
import com.softeng.dingtalk.dao.mapper.InternalPaperMapper;

import com.softeng.dingtalk.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Value("${paper.acDeductionRate}")
    private double acDeductionRate;
    @Value("${paper.rank1Rate}")
    private double rank1Rate;
    @Value("${paper.rank2Rate}")
    private double rank2Rate;
    @Value("${paper.rank3Rate}")
    private double rank3Rate;
    @Value("${paper.rankDefaultRate}")
    private double rankDefaultRate;
    @Value("${paper.suspendACPunishment}")
    private double suspendACPunishment;
    @Autowired
    BaseApi baseApi;

    /**
     * 根据 internalPaper 和 List<AuthorVO> 生成 PaperDetails
     *
     * @param paper   实验室内部论文
     * @param authors 论文作者VO list
     * @return
     */
    public List<PaperDetailPo> setPaperDetailsByAuthorsAndPaper(InternalPaperPo paper, List<AuthorVO> authors) {
        return authors.stream()
                .map(author -> new PaperDetailPo(paper, author.getUid(), author.getNum()))
                .collect(Collectors.toList());
    }


    /**
     * 添加实验室内部论文
     * 
     * @param vo 实验室内部论文VO对象
     */
    public void addInternalPaper(InternalPaperVO vo) {
        InternalPaperPo internalPaperPo = new InternalPaperPo(vo.getTitle(), vo.getJournal(), vo.getPaperType(), vo.getIssueDate(),
                vo.getIsStudentFirstAuthor(), vo.getFirstAuthor(),vo.getPath(),vo.getTheme(),vo.getYear());
        if (!internalPaperPo.getIsStudentFirstAuthor()) {
            internalPaperPo.setResult(InternalPaperPo.REVIEWING);
            internalPaperPo.setSubmissionFileName(vo.getFileName());
            internalPaperPo.setSubmissionFileId(vo.getFileId());
        }else {
            internalPaperPo.setReviewFileName(vo.getFileName());
            internalPaperPo.setReviewFileId(vo.getFileId());
        }
        internalPaperRepository.save(internalPaperPo);
        paperDetailRepository.saveBatch(setPaperDetailsByAuthorsAndPaper(internalPaperPo, vo.getAuthors()));
    }


    /**
     * 添加实验室外部论文
     *
     * @param vo 实验室外部论文vo对象
     */
    public void addExternalPaper(ExternalPaperVO vo) {
        // 创建对应的外部论文对象
        ExternalPaperPo externalPaperPo = new ExternalPaperPo(vo.getTitle());
        externalPaperPo.setReviewFileName(vo.getFileName());
        externalPaperPo.setReviewFileId(vo.getFileId());
        externalPaperPo.setTheme(vo.getTheme());
        externalPaperPo.setPath(vo.getPath());
        externalPaperRepository.save(externalPaperPo);
        // 创建外部论文对应的投票
        VotePo votePo = new VotePo(vo.getStartTime(), vo.getEndTime(), true, externalPaperPo.getId());
        externalPaperPo.setVote(votePo);

        voteRepository.save(votePo);
    }


    /**
     * 更新内部论文记录
     *
     * @param vo
     */
    public void updateInternalPaper(InternalPaperVO vo) {
        InternalPaperPo internalPaperPo = internalPaperRepository.findById(vo.getId()).get();
        // 1. 更新 paper 信息
        internalPaperPo.update(vo.getTitle(), vo.getJournal(), vo.getPaperType(), vo.getIssueDate(), vo.getFirstAuthor());
        // 2. 删除旧的paperDetail
        paperDetailRepository.deleteByInternalPaper(internalPaperPo);
        // 3. 插入新的paperDetail
        internalPaperPo.setPaperDetails(setPaperDetailsByAuthorsAndPaper(internalPaperPo, vo.getAuthors()));
        // 4. 重新计算ac
        paperService.calculateInternalPaperAc(internalPaperPo);
        // 5. 重新添加paperDetail
        paperDetailRepository.saveBatch(internalPaperPo.getPaperDetails());
        internalPaperRepository.save(internalPaperPo);
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
        paperDetailRepository.deleteByInternalPaper(new InternalPaperPo(id));
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
                return rank1Rate;
            case 2:
                return rank2Rate;
            case 3:
                return rank3Rate;
            default:
                return rankDefaultRate;
        }
    }

    /**
     * 按照论文投票投稿结果计算ac权重
     * 投稿接受正常算，平票中止和投稿被拒扣一半分
     * @param internalPaperPo
     * @return
     */
    public double calculateWeightOfAc(InternalPaperPo internalPaperPo) {
        switch (internalPaperPo.getResult()) {
            case InternalPaperPo.ACCEPT:
                return 1.0;
            case InternalPaperPo.SUSPEND:
            case InternalPaperPo.REJECT:
                return -acDeductionRate;
            default:
                return 0.0;
        }
    }

    /**
     * 计算某个作者的 AC 加减分, 根据论文投稿情况、该论文类型对应的 AC 奖池、作者排名
     * 论文接受加正分，拒绝或者中止扣一半分
     *
     * @param internalPaperPo 论文投稿情况
     * @param sum      AC 奖池
     * @param rank     作者排名
     * @return AC值
     */
    public double calculateAc(InternalPaperPo internalPaperPo, double sum, int rank) {
        return calculateWeightOfAc(internalPaperPo) * calculateRatioOfAc(rank) * sum;
    }

    /**
     * 计算论文结果对应的 AC
     */
    public void calculateInternalPaperAc(InternalPaperPo internalPaperPo) {
        int result = internalPaperPo.getResult();
        if(result != InternalPaperPo.ACCEPT
            && result != InternalPaperPo.REJECT
            && result != InternalPaperPo.SUSPEND) {
            log.info("论文没有处在计算ac的状态");
            return;
        }
        if(internalPaperPo.hasAccepted() && !internalPaperPo.hasCompleteFile()) {
            log.info("论文文件不完整，无法生成ac");
            return;
        }
        // 1. 获取 paperDetails
        log.info("获取 paperDetails");
        var paperDetails = internalPaperPo.getPaperDetails();

        // 2. 删除 paperDetail 对应的旧的 AcRecord. 防止admin后续修改作者信息导致混乱
        log.info("删除 paperDetail 对应的旧的 AcRecord");
        acRecordRepository.deleteAll(
                paperDetails.stream()
                        .map(PaperDetailPo::getAcRecord)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );

        // 3. 查询该类型论文对应的总 AC
        log.info("查询该类型论文对应的总 AC");
        double sum = paperLevelRepository.getValue(internalPaperPo.getPaperType());

        // 4. 更新 paperDetail 对应的 AcRecord
        log.info("更新 paperDetail 对应的 AcRecord");
        paperDetails.forEach(paperDetailPo -> {
            paperDetailPo.setAcRecord(new AcRecordPo(
                    paperDetailPo.getUser(),
                    null,
                    calculateAc(internalPaperPo, sum, paperDetailPo.getNum()),
                    internalPaperPo.getReason(),
                    AcRecordPo.PAPER,
                    internalPaperPo.getUpdateDate().atTime(8, 0)
            ));
        });

        // 6. 更新paperDetails表和acRecord表
        log.info("更新paperDetails表和acRecord表");
        acRecordRepository.saveAll(
                paperDetails.stream()
                        .map(PaperDetailPo::getAcRecord)
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
        log.info("获取对应的内部论文");
        InternalPaperPo internalPaperPo = internalPaperRepository.findById(id).get();

        // 2. 校验论文投票和投稿情况
        log.info("校验论文投票和投稿情况");
        if(internalPaperPo.getIsStudentFirstAuthor()) {
            if (internalPaperPo.getVote().getResult() == -1 || internalPaperPo.getVote().getResult() == 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "内审投票未结束或未通过！");
            }
        }

        if(internalPaperPo.getResult() == InternalPaperPo.FLAT
            || internalPaperPo.getResult() == InternalPaperPo.SUSPEND) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "作者未决定投稿或已中止投稿！");
        }

        // 3. 更新指定论文的投稿结果和更新时间
        log.info("更新指定论文的投稿结果和更新时间");
        internalPaperPo.setResult(this.getPaperResult(true,result));
        internalPaperPo.setUpdateDate(updateDate);
        internalPaperRepository.save(internalPaperPo);
        // 4. 更新论文 ac
        log.info("更新论文 ac");
        paperService.calculateInternalPaperAc(internalPaperPo);

        // 5. 插入相关消息
        notifyService.paperAcMessage(internalPaperPo);
    }


    /**
     * 更新外部论文投稿结果
     *
     * @param id
     * @param result
     * @param updateDate
     */
    public void updateExPaperResult(int id, int result, LocalDate updateDate) {
        ExternalPaperPo externalPaperPo = externalPaperRepository.findById(id).get();

        if (externalPaperPo.getVote().getResult() == -1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "投票尚未结束");
        }

        //更新论文的结果
        externalPaperPo.setResult(getPaperResult(false,result));
        externalPaperPo.setUpdateDate(updateDate);
        externalPaperRepository.save(externalPaperPo);
    }

    private int getPaperResult(boolean isPaper,int result){
        if(isPaper){
            switch (result){
                case 0:
                    return 3;
                case 1:
                    return 4;
                case 2:
                    return 6;
                default:
                    return result;
            }
        } else {
            return result;
        }
    }

    /**
     * @param page
     * @param size
     * @return
     */
    public Page<ExternalPaperPo> listExternalPaper(int page, int size) {
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
    public InternalPaperPo getInternalPaper(int id) {
        InternalPaperPo internalPaperPo = internalPaperRepository.findById(id).get();
        internalPaperPo.setPaperDetails(paperDetailRepository.findByInternalPaper(internalPaperPo));
        return internalPaperPo;
    }

    /**
     * 根据指定id 查询外部评审论文
     *
     * @param id
     * @return
     */
    public ExternalPaperPo getExInternalPaper(int id) {
        return externalPaperRepository.findById(id).get();
    }


    /**
     * 获取论文对应的投票
     *
     * @param pid
     * @return
     */
    public VotePo getVoteByPid(int pid) {
        return internalPaperRepository.findVoteById(pid);
    }


    /**
     * 提交论文评审建议
     *
     * @param reviewPo
     * @param uid
     * @return
     */
    public void submitReview(ReviewPo reviewPo, int uid) {
        UserPo userPo = new UserPo(uid);
        reviewPo.setUser(userPo);
        reviewRepository.save(reviewPo);
    }

    /**
     * @param paperId
     * @param isExternal
     * @return
     */
    public List<ReviewPo> listReview(int paperId, boolean isExternal) {
        return reviewRepository.findAllByPaperidAndExternal(paperId, isExternal);
    }


    /**
     * 更新评审意见
     *
     * @param reviewPo 被更新的评审细节
     */
    public void updateReview(ReviewPo reviewPo) {
        reviewRepository.save(reviewPo);
        log.debug(reviewPo.getUpdateTime().toString());
    }


    /**
     * 删除评审意见
     *
     * @param id  评审的id
     * @param uid 要删除的用户的id
     */
    public void deleteReview(int id, int uid) {
        ReviewPo reviewPo = reviewRepository.findById(id).get();
        if (uid != reviewPo.getUser().getId()) {
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
    public VotePo getExPaperVote(int id) {
        return externalPaperRepository.findById(id).get().getVote();
    }

    /**
     * @Description
     * @Author Jerrian Zhao
     * @Data 02/10/2022
     */

    public void decideFlat(FlatDecisionVO flatDecisionVO) {
        InternalPaperPo internalPaperPo = internalPaperRepository.findById(flatDecisionVO.getId()).get();
        if (flatDecisionVO.getDecision()) {
            internalPaperPo.setFlatDecision(1);
            internalPaperPo.setResult(2);
            internalPaperRepository.save(internalPaperPo);
        } else {
            internalPaperPo.setFlatDecision(0);
            internalPaperPo.setResult(6); //投稿中止
            internalPaperPo.setUpdateDate(LocalDate.now());
            internalPaperRepository.save(internalPaperPo);
            // 更新论文 ac
            paperService.calculateInternalPaperAc(internalPaperPo);
            // 插入相关消息
            notifyService.paperAcMessage(internalPaperPo);
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
