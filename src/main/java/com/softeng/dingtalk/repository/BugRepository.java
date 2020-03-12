package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhanyeye
 * @description
 * @date 3/12/2020
 */
@Repository
public interface BugRepository extends JpaRepository<Bug, Integer> {
}
