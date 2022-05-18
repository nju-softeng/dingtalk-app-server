package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.entity.VMApply;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VMApplyRepository extends CustomizedRepository<VMApply,Integer>{
    List<VMApply> findAllByStateEquals(int state);
    List<VMApply> findAllByUserEquals(User user);
}
