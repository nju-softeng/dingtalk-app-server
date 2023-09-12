package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.entity.User;
import com.softeng.dingtalk.entity.VMApply;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VMApplyRepository extends CustomizedRepository<VMApply,Integer>, JpaSpecificationExecutor<VMApply> {
    List<VMApply> findAllByStateEquals(int state);
    List<VMApply> findAllByUserEquals(User user);
}
