package com.softeng.dingtalk.dao.repository;

import com.softeng.dingtalk.po.BugPo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanyeye
 * @description
 * @date 3/12/2020
 */
@Repository
public interface BugRepository extends CustomizedRepository<BugPo, Integer> {
    @EntityGraph(value="bug.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select b from BugPo b where b.project.id = :pid order by b.id desc")
    List<BugPo> findAllByProjectId(@Param("pid") int pid);

    @EntityGraph(value="bug.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select b from BugPo b where b.project.auditor.id = :aid order by b.id desc")
    List<BugPo> listBugByAuditor(@Param("aid") int aid);

    /**
     * 查询审核人待审的bug数
     * @param aid
     * @return
     */
    @Query("select count (b.id) from BugPo b where b.project.auditor.id = :aid and b.status is null ")
    Integer getAuditorPendingBugCnt(@Param("aid") int aid);

    @Modifying
    @Query("update BugPo set status = :status where id = :id")
    void updateBugStatus(@Param("id") int id, @Param("status") boolean status);


    @EntityGraph(value="bug.graph",type= EntityGraph.EntityGraphType.FETCH)
    @Query("select b from BugPo b where b.id in :ids order by b.id desc")
    List<BugPo> findAllById(@Param("ids") List<Integer> ids);



}
