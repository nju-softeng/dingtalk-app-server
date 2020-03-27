package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 2/5/2020
 */
@Repository
public interface VoteRepository extends CustomizedRepository<Vote, Integer> {

    //拿到所有状态没有结束的投票
    @Query("select v from Vote v where v.status = false ")
    List<Vote> listByStatus();

    @Modifying
    @Query("update Vote v set v.status = true, v.accept = :accept, v.total = :total, v.result = :result where v.id = :id")
    void updateStatus(@Param("id") int id, @Param("accept") int accept, @Param("total") int total, @Param("result") boolean result);


}
