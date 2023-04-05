package com.softeng.dingtalk.controller;


import com.alibaba.fastjson.JSONObject;
import com.softeng.dingtalk.po_entity.ExternalPaper;
import com.softeng.dingtalk.po_entity.InternalPaper;
import com.softeng.dingtalk.po_entity.Review;
import com.softeng.dingtalk.po_entity.Vote;
import com.softeng.dingtalk.dao.repository.ExternalPaperRepository;
import com.softeng.dingtalk.dao.repository.InternalPaperRepository;
import com.softeng.dingtalk.dao.repository.VoteRepository;
import com.softeng.dingtalk.service.FileService;
import com.softeng.dingtalk.service.PaperService;
import com.softeng.dingtalk.service.VoteService;
import com.softeng.dingtalk.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @Autowired
    InternalPaperRepository internalPaperRepository;
    @Autowired
    FileService fileService;



    /**
     * 添加或更新实验室内部及非学生一作论文记录
     * @param file
     * @param paperFormJsonStr
     */
    @PostMapping("/paper")
    public void addPaper(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "paperFormJsonStr") String paperFormJsonStr, @RequestAttribute int uid) {
        log.info("uid: "+ uid);
        InternalPaperVO vo= JSONObject.parseObject(paperFormJsonStr,InternalPaperVO.class);
        if (vo.getId() == null) {
            vo.setFileName(file.getOriginalFilename());
            String fileId = fileService.addFileByPath(file, vo.getPath() + (vo.getIsStudentFirstAuthor() ? "/Review" : "/Submission"));
            vo.setFileId(fileId);
            paperService.addInternalPaper(vo);
        } else {
            paperService.updateInternalPaper(vo);
        }
    }

    /**
     * 添加外部评审论文记录
     * @param file
     * @param externalPaperFormJsonStr
     * @param uid
     */
    @PostMapping("/ex-paper")
    public void addExternalPaper(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "externalPaperFormJsonStr") String externalPaperFormJsonStr, @RequestAttribute int uid) {
        ExternalPaperVO vo=JSONObject.parseObject(externalPaperFormJsonStr,ExternalPaperVO.class);
        vo.setFileName(file.getOriginalFilename());
        String fileId=fileService.addFileByPath(file,vo.getPath()+"/Review");
        vo.setFileId(fileId);
        if (vo.getId() == null) {
            paperService.addExternalPaper(vo);
        } else {
            paperService.updateExternalPaper(vo);
        }
    }

    /**
     * 删除论文记录
     * @param id
     */
    @GetMapping("/paper/delete/{id}")
    public void deletePaper(@PathVariable int id) {
        paperService.deleteInternalPaper(id);
    }


    /**
     * 删除指定的外部论文
     * @param id
     */
    @GetMapping("/ex-paper/rm/{id}")
    public void deleteExternalPaper(@PathVariable int id) {
        paperService.deleteExternalPaper(id);
    }

    /**
     * 论文参与者或审核人更新论文投稿结果
     * @param pid
     * @param vo
     */
    @PostMapping("/paper_result/{pid}")
    public void updateResult(@PathVariable int pid, @RequestBody PaperResultVO vo) {
        paperService.updateInternalPaperResult(pid, vo.getResult(), vo.getUpdateDate());
        Vote vote = internalPaperRepository.findVoteById(pid);
        if(vote != null) {
            voteService.computeVoteAc(vote, vo.getResult(), LocalDateTime.of(vo.getUpdateDate(), LocalTime.of(8, 0)));
        }
    }


    /**
     * 更新外部论文的评审状态
     * @param pid
     * @param
     */
    @PostMapping("/ex-paper_result/{pid}")
    public void updateExPaperResult(@PathVariable int pid, @RequestBody PaperResultVO vo) {
        // 更新论文记录
        paperService.updateExPaperResult(pid, vo.getResult(), vo.getUpdateDate());
        Vote vote = externalPaperRepository.findVoteById(pid);
        // 更具投票结果计算，投票人的ac值
        voteService.computeVoteAc(vote, vo.getResult(), LocalDateTime.of(vo.getUpdateDate(), LocalTime.of(8,0)));
    }


    /**
     * 分页获取论文
     * @param page
     * @return
     */
    @GetMapping("/paper/page/{page}/{size}")
    public Map listPaper(@PathVariable int page, @PathVariable int size) {
        return paperService.listInternalPaper(page, size);
    }

    /**
     * 查询所有的评审投票
     * @return
     */
    @GetMapping("/ex-paper/page/{page}/size/{size}")
    public Map listExternalPaper(@PathVariable int page, @PathVariable int size) {
        Page<ExternalPaper> pages = paperService.listExternalPaper(page, size);
        return Map.of("list", pages.getContent(), "total", pages.getTotalElements());
    }


    /**
     * 获取某个论文的详细信息
     * @param id
     * @return
     */
    @GetMapping("/paper/{id}")
    public InternalPaper getPaper(@PathVariable int id) {
        return paperService.getInternalPaper(id);
    }


    /**
     * 查询指定id的ExternalPaper
     * @param id
     * @return
     */
    @GetMapping("/ex-papper/{id}")
    public ExternalPaper getExPaper(@PathVariable int id) {
        return paperService.getExInternalPaper(id);
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
     * 查询指内部论文的评审意见
     * @param id
     * @return
     */
    @GetMapping("/paper/{id}/review")
    public List<Review> listReview(@PathVariable int id) {
        return paperService.listReview(id, false);
    }


    /**
     * 查询指内部论文的评审意见
     * @param id
     * @return
     */
    @GetMapping("/ex-paper/{id}/review")
    public List<Review> listExReview(@PathVariable int id) {
        return paperService.listReview(id, true);
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





    /**
     * 查询指定id的ExternalPaper
     * @param pid
     * @return
     */
    @GetMapping("/ex-paper/{pid}/vote")
    public Vote getExPaperVote(@PathVariable int pid) {
        return paperService.getExPaperVote(pid);
    }

    /**
     * @Description
     * @Author Jerrian Zhao
     * @Data 02/10/2022
     */

    /**
     * 指定是否接受平票论文
     */
    @PatchMapping("/paper/{id}")
    public void decideFlat(@RequestBody FlatDecisionVO flatDecisionVO){
        paperService.decideFlat(flatDecisionVO);
    }


    /**
     * 分页获取非学生一作
     * @param page
     * @return
     */
    @GetMapping("/non-first/page/{page}/{size}")
    public Map listNonFirstPaper(@PathVariable int page, @PathVariable int size) {
        return paperService.listNonFirstPaper(page, size);
    }
}
