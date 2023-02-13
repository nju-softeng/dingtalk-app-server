package com.softeng.dingtalk.controller;

import com.softeng.dingtalk.po.PrizePo;
import com.softeng.dingtalk.service.PrizeService;
import com.softeng.dingtalk.vo.PrizeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 获奖情况
 * @Author Jerrian Zhao
 * @Data 01/25/2022
 */

@Slf4j
@RestController
@RequestMapping("/api/users")
public class PrizeController {
    @Autowired
    PrizeService prizeService;

    /**
     * 获取用户的获奖情况
     *
     * @param uid
     * @return
     */
    @GetMapping("{uid}/prizes")
    public List<PrizePo> findByUserId(@PathVariable int uid) {
        return prizeService.findByUser(uid);
    }

    /**
     * 新增奖项
     * @param uid
     * @param prizeVO
     */
    @PostMapping("/{uid}/prizes")
    public void addNewPrize(@PathVariable int uid, @RequestBody PrizeVO prizeVO){
        prizeService.addNewPrize(uid,prizeVO);
    }

    /**
     * 更新奖项信息
     * @param prizeVO
     */
    @PutMapping("/{uid}/prizes/{prizeId}")
    public void updatePrize(@RequestBody PrizeVO prizeVO){
        prizeService.updatePrize(prizeVO);
    }

    /**
     * 删除奖项
     * @param prizeId
     */
    @DeleteMapping("/{uid}/prizes/{prizeId}")
    public void deletePrize(@PathVariable int prizeId){
        prizeService.deletePrize(prizeId);
    }
}
