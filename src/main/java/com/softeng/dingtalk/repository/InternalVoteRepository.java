package com.softeng.dingtalk.repository;

import com.softeng.dingtalk.entity.InternalVote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 2/5/2020
 */
@Repository
public interface InternalVoteRepository extends CustomizedRepository<InternalVote, Integer> {

    /**
     * 查询所有状态没有结束的投票
     * @return 放回所有未结束的投票
     */
    @Query("select iv from InternalVote iv where iv.status = false ")
    List<InternalVote> listByStatusIsFalse();


    /**
     * 当投票截至后更新投票结果
     * @param id 投票id
     * @param accept 接收人数
     * @param total  总人数
     * @param result 最终结果
     */
    @Modifying
    @Query("update InternalVote iv set iv.status = true, iv.accept = :accept, iv.total = :total, iv.result = :result where iv.id = :id")
    void updateStatus(int id, int accept, int total, boolean result);

    /**
     * 查询指定论文的投票是否已经存在
      * @param pid
     * @return
     */
    @Query(value = "select ifnull((select id from internal_vote where pid =:pid limit 1 ), 0)", nativeQuery = true)
    Integer isExisted(int pid);



}
