package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.PracticePo;
import com.softeng.dingtalk.po.UserPo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends CustomizedRepository<PracticePo,Integer>{
    List<PracticePo> findAllByStateEquals(int state);
    List<PracticePo> findAllByUserEquals(UserPo userPo);
}
