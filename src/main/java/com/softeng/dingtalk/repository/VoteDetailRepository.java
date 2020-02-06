package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.VoteDetail;
import com.softeng.dingtalk.vo.VoteResultVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zhanyeye
 * @description
 * @date 2/5/2020
 */
@Repository
public interface VoteDetailRepository extends JpaRepository<VoteDetail, Integer> {
//    @Query("select count ")
//    VoteResultVO getVoteResult(@Param("id") int id)
}
