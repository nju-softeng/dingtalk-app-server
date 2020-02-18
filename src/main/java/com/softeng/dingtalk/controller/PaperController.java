package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.projection.PaperProjection;
import com.softeng.dingtalk.service.PaperService;
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

    // 添加论文记录
    @PostMapping("/paper")
    public void addPaper(@RequestBody PaperVO papervo) {
        log.debug("/paper");
        paperService.addPaper(papervo);
    }

    //更新论文记录
    @PatchMapping("/paper")
    public void updatePaper(@RequestBody PaperVO papervo) {
        paperService.updatePaper(papervo);
    }

    //删除论文记录
    @DeleteMapping("/paper/{id}")
    public void deletePaper(@PathVariable int id) {
        paperService.deletePaper(id);
    }


    // 论文参与者或审核人更新论文投稿结果
    @PostMapping("/paper_result/{id}")
    public void updateResult(@PathVariable int id, @RequestBody Integer result) {
        paperService.updateResult(id, result);
        //todo  更新论文投票结果
    }

    @GetMapping("/paper/page/{page}")
    public Map listPaper(@PathVariable int page) {
        return paperService.listPaper(page);
    }


    @GetMapping("/paper/{id}")
    public Paper getPaper(@PathVariable int id) {
        return paperService.getPaper(id);
    }






}
