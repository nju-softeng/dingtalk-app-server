package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.entity.Practice;
import com.softeng.dingtalk.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends CustomizedRepository<Practice,Integer>, JpaSpecificationExecutor<Practice> {
    List<Practice> findAllByStateEquals(int state);
    List<Practice> findAllByUserEquals(User user);
}
