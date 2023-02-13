package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.UserPo;
import com.softeng.dingtalk.po.VMApplyPo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VMApplyRepository extends CustomizedRepository<VMApplyPo,Integer>{
    List<VMApplyPo> findAllByStateEquals(int state);
    List<VMApplyPo> findAllByUserEquals(UserPo userPo);
}
