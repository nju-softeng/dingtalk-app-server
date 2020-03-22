package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.BugDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhanyeye
 * @description
 * @date 3/22/2020
 */
@Repository
public interface BugDetailRepository extends JpaRepository<BugDetail, Integer> {
    void deleteBugDetailByBugId(int id);
}
