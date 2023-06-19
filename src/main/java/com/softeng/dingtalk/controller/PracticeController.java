package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.dto.CommonResult;
import com.softeng.dingtalk.dto.req.PracticeReq;
import com.softeng.dingtalk.entity.Practice;
import com.softeng.dingtalk.service.PracticeService;
import com.softeng.dingtalk.vo.PracticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class PracticeController {
    @Autowired
    PracticeService practiceService;
    @PostMapping("/practice")
    public void addPractice(@RequestBody PracticeVO practiceVO, @RequestAttribute int uid){
        if(practiceVO.getId()==null){
            practiceService.addPractice(practiceVO,uid);
        } else {
            practiceService.modifyPractice(practiceVO);
        }
    }
    @GetMapping("/practice")
    public List<Practice> getPracticeList(@RequestAttribute int uid) {
        return practiceService.getPracticeList(uid);
    }


    @DeleteMapping("/practice/{id}")
    public void deletePractice(@PathVariable int id,@RequestAttribute int uid) {
        practiceService.deletePractice(id,uid);
    }

    @PostMapping("/v2/practice/{page}/{size}")
    public CommonResult<Map<String, Object>> queryPracticeList(@PathVariable int page, @PathVariable int size, @RequestBody PracticeReq practiceReq) {
        return CommonResult.success(practiceService.queryPracticeList(page, size, practiceReq));
    }

    @PostMapping("/v2/practice")
    public CommonResult<String> addPractice(@RequestBody PracticeReq practiceReq){
        practiceService.addPractice(practiceReq);
        return CommonResult.success("新增实习申请成功");
    }

    @PutMapping("/v2/practice")
    public CommonResult<String> modifyPractice(@RequestBody PracticeReq practiceReq){
        practiceService.modifyPractice(practiceReq);
        return CommonResult.success("编辑实习申请成功");
    }
}
