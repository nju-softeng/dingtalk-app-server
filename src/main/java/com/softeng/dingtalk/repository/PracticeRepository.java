package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Practice;
import com.softeng.dingtalk.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends CustomizedRepository<Practice,Integer>{
    List<Practice> findAllByStateEquals(int state);
    List<Practice> findAllByUserEquals(User user);
}
