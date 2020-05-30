package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.projection.PaperProjection;
import com.softeng.dingtalk.service.PaperService;
import com.softeng.dingtalk.service.VoteService;
import com.softeng.dingtalk.vo.PaperVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/paper/page/{page}")
    public Map listPaper(@PathVariable int page) {
        return paperService.listPaper(page);
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


}
