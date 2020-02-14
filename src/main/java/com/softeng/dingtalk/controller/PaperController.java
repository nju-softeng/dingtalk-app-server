package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.entity.Paper;
import com.softeng.dingtalk.projection.PaperProjection;
import com.softeng.dingtalk.service.PaperService;
import com.softeng.dingtalk.vo.PaperVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @PostMapping("/paper_result/{id}")
    public void updateResult(@PathVariable int id, @RequestBody Integer result) {
        paperService.updateResult(id, result);
    }

    @GetMapping("/paper")
    public List<Paper> listPaper() {
        return paperService.listPaper();
    }


    @GetMapping("/paper/{id}")
    public Paper getPaper(@PathVariable int id) {
        return paperService.getPaper(id);
    }


    @GetMapping("/papertest")
    public List<PaperProjection> func() {
        return paperService.test();
    }



}
