package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.service.BlockChainService;
import com.softeng.dingtalk.vo.ConflictVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class BlockChainController {
    @Autowired
    BlockChainService blockChainService;
    @GetMapping("/verification")
    public List<ConflictVO> checkACRecord(){
        return blockChainService.checkACRecord();
    }

    @PostMapping("/verification")
    public void decideConflict(@RequestBody ConflictVO conflictVO){
        blockChainService.decideConflict(conflictVO);
    }
}
