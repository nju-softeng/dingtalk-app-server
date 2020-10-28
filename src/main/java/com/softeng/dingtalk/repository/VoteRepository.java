package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 2/5/2020
 */
@Repository
public interface VoteRepository extends CustomizedRepository<Vote, Integer> {

    @Query("select v from Vote v where v.external = true and v.isStarted = false and v.startTime <= :nowtime")
    List<Vote> listUpcomingVote(LocalDateTime nowtime);

    /**
     * 查询所有状态没有结束的投票
     * @return 所有未结束的投票
     */
    @Query("select v from Vote v where v.status = false ")
    List<Vote> listByStatusIsFalse();


    /**
     * 当投票截至后更新投票结果
     * @param id 投票id
     * @param accept 接收人数
     * @param total  总人数
     * @param result 最终结果
     */
    @Modifying
    @Query("update Vote v set v.status = true, v.accept = :accept, v.total = :total, v.result = :result where v.id = :id")
    void updateStatus(int id, int accept, int total, boolean result);


    /**
     * 查询指定论文的投票是否已经存在
     * @param pid 论文id
     * @param external 是内部评审还是外部评审
     * @return
     */
    @Query(value = "select ifnull((select id from vote where pid =:pid and external = :external limit 1 ), 0)", nativeQuery = true)
    Integer isExisted(int pid, boolean external);



}
