package com.softeng.dingtalk.controller;


import com.softeng.dingtalk.entity.ExternalPaper;
import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.entity.Review;
import com.softeng.dingtalk.entity.Vote;
import com.softeng.dingtalk.repository.ExternalPaperRepository;
import com.softeng.dingtalk.repository.VoteRepository;
import com.softeng.dingtalk.service.PaperService;
import com.softeng.dingtalk.service.VoteService;
import com.softeng.dingtalk.vo.ExternalPaperVO;
import com.softeng.dingtalk.vo.PaperVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * @author zhanyeye
 * @description
 * @create 2/5/2020 4:14 PM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class PaperController {
    @Autowired
    PaperService paperService;
    @Autowired
    VoteService voteService;
    @Autowired
    ExternalPaperRepository externalPaperRepository;
    @Autowired
    VoteRepository voteRepository;

    /////////////////
    // 内部论文评审操作
    /////////////////

    /**
     * 添加或更新论文记录
     * @param papervo
     */
    @PostMapping("/paper")
    public void addPaper(@RequestBody PaperVO papervo) {
        log.debug("/paper");
        if (papervo.getId() == null) {
            log.debug("add paper");
            paperService.addPaper(papervo);
        } else {
            paperService.updatePaper(papervo);
            log.debug("update paper");
        }

    }


    /**
     * 删除论文记录
     * @param id
     */
    @GetMapping("/paper/delete/{id}")
    public void deletePaper(@PathVariable int id) {
        paperService.deletePaper(id);
    }


    /**
     * 论文参与者或审核人更新论文投稿结果
     * @param pid
     * @param map
     */
    @PostMapping("/paper_result/{pid}")
    public void updateResult(@PathVariable int pid, @RequestBody Map<String, Boolean> map) {
        paperService.updateResult(pid, map.get("data"));
        voteService.computeVoteAc(pid, map.get("data"));
        // todo 发送论文消息
    }


    /**
     * 分页获取论文
     * @param page
     * @return
     */
    @GetMapping("/paper/page/{page}/{size}")
    public Map listPaper(@PathVariable int page, @PathVariable int size) {
        return paperService.listPaper(page, size);
    }


    /**
     * 获取某个论文的详细信息
     * @param id
     * @return
     */
    @GetMapping("/paper/{id}")
    public Paper getPaper(@PathVariable int id) {
        return paperService.getPaper(id);
    }


    /**
     * 提交评审记录
     * @return
     */
    @PostMapping("/paper/review")
    public void submitReview(@RequestBody Review review, @RequestAttribute int uid) {
        paperService.submitReview(review, uid);
    }


    /**
     * 查询指定论文的评审意见
     * @param id
     * @return
     */
    @GetMapping("/paper/{id}/review")
    public List<Review> listReview(@PathVariable int id) {
        return paperService.listReview(id);
    }


    /**
     * 更新评审建议
     * @param review
     * @param uid
     */
    @PostMapping("/paper/{id}/review/update")
    public void updateReview(@RequestBody Review review, @RequestAttribute int uid) {
        if (uid != review.getUser().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "无修改权限");
        }
        paperService.updateReview(review);
    }


    /**
     * 删除评审意见
     * @param id
     * @param uid
     */
    @GetMapping("/paper/review/delete/{id}")
    public void deleteReview(@PathVariable int id, @RequestAttribute int uid) {
        paperService.deleteReview(id, uid);
    }


    /**
     * 获取论文投票
     * @param id
     * @return
     */
    @GetMapping("/paper/{id}/vote")
    public Vote getPaperVote(@PathVariable int id) {
        return paperService.getVoteByPid(id);
    }


    /////////////////
    // 外部论文评审操作
    /////////////////

    @PostMapping("/ex-paper")
    public void addExternalPaper(@RequestBody ExternalPaperVO vo) {
        if (vo.getId() == null) {
            // 首先创建一个外部论文
            ExternalPaper externalPaper = new ExternalPaper(vo.getTitle());
            externalPaperRepository.save(externalPaper);
            // 再创建一个投票
            Vote vote = new Vote(vo.getStartTime(), vo.getEndTime(), true, externalPaper.getId());
            voteRepository.save(vote);
            externalPaper.setVote(vote);
            externalPaperRepository.save(externalPaper);
        } else {

        }


    }

    @GetMapping("/ex-paper/rm/{id}")
    public void deleteExternalPaper(@PathVariable int id) {
        paperService.deleteExternalPaper(id);
    }


    /**
     * 查询所有的评审投票
     * @return
     */
    @GetMapping("/ex-paper/list")
    public List<ExternalPaper> listExternalPaper() {
        List<ExternalPaper> kk = paperService.listExternalPaper();
        return paperService.listExternalPaper();
    }






}
