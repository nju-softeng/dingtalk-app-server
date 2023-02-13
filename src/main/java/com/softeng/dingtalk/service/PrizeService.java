package com.softeng.dingtalk.service;

import com.softeng.dingtalk.po.PrizePo;
import com.softeng.dingtalk.po.UserPo;
import com.softeng.dingtalk.dao.repository.PrizeRepository;
import com.softeng.dingtalk.vo.PrizeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description 获奖情况业务逻辑
 * @Author Jerrian Zhao
 * @Data 01/25/2022
 */

@Service
@Transactional
@Slf4j
public class PrizeService {
    @Autowired
    PrizeRepository prizeRepository;

    /**
     * 查询用户所有奖项
     * @param userId
     * @return
     */
    public List<PrizePo> findByUser(int userId){
        return prizeRepository.findByUserId(userId);
    }

    /**
     * 新增奖项
     * @param userId
     * @param prizeVO
     * @return
     */
    public PrizePo addNewPrize(int userId, PrizeVO prizeVO){
        PrizePo prizePo =new PrizePo(new UserPo(userId),prizeVO.getPrizeTime(),prizeVO.getPrizeName(),prizeVO.getLevel(),prizeVO.getRemark());
        return prizeRepository.save(prizePo);
    }

    /**
     * 更新奖项
     * @param prizeVO
     */
    public void updatePrize(PrizeVO prizeVO){
        PrizePo p = prizeRepository.findById(prizeVO.getId()).get();
        p.setLevel(prizeVO.getLevel());
        p.setPrizeTime(prizeVO.getPrizeTime());
        p.setPrizeName(prizeVO.getPrizeName());
        p.setRemark(prizeVO.getRemark());
        prizeRepository.save(p);
    }

    /**
     * 删除奖项
     * @param prizeId
     */
    public void deletePrize(int prizeId){
        PrizePo p = prizeRepository.findById(prizeId).get();
        p.setDeleted(true);
        prizeRepository.save(p);
    }
}
