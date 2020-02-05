package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhanyeye
 * @description
 * @date 2/5/2020
 */
@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
}
