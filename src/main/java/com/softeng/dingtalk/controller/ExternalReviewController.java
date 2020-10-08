package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.ExternalVote;
import com.softeng.dingtalk.service.ExternalReviewService;
import com.softeng.dingtalk.vo.ExternalVoteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 处理关于外部评审的请求
 * @author: zhanyeye
 * @create: 2020-10-08 19:14
 **/
@Slf4j
@RestController
@RequestMapping("/api")
public class ExternalReviewController {
    @Autowired
    ExternalReviewService externalReviewService;

    /**
     * 创建新的外部评审投票
     * @param externalVote
     */
    @PostMapping("/ex-review")
    public void addExternalVote(@RequestBody ExternalVote externalVote) {
        log.debug(externalVote.toString());
        externalReviewService.addExternalVote(externalVote);

    }

    @PostMapping("/ex-review/update")
    public void updateExternalVote(@RequestBody ExternalVoteVO externalVoteVO) {

    }

    /**
     * 查询所有的评审投票
     * @return
     */
    @GetMapping("/ex-review/list")
    public List<ExternalVote> listExternalVote() {
        return externalReviewService.listExternalVote();
    }

    public void deleteExternalVote() {

    }

}
