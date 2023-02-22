package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po_entity.User;
import com.softeng.dingtalk.po_entity.VMApply;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VMApplyRepository extends CustomizedRepository<VMApply,Integer>{
    List<VMApply> findAllByStateEquals(int state);
    List<VMApply> findAllByUserEquals(User user);
}
